package main

import (
	"time"
	"fmt"
	"os/exec"
	"sync"
	"bytes"

	"github.com/stats"
)

const MILLIS_IN_SECOND = 1000

func worker(requests int, completeCh chan time.Duration) {
	for i := 0; i < requests; i++ {
		start := time.Now()

//		cmd := exec.Command("docker","-H", "tcp://0.0.0.0:2376","run", "-itd", "-c", "0", "-m", "0", "-e", "affinity:requestclass==4", "-e", "affinity:requesttype==job", "ubuntu")
		cmd := exec.Command("docker","-H", "tcp://0.0.0.0:2376","run", "-itd", "-c", "0", "-m", "0", "ubuntu")
		var out, stderr bytes.Buffer
		cmd.Stdout = &out
		cmd.Stderr = &stderr

		if err := cmd.Run(); err != nil {
			fmt.Println("Error using docker run at rescheduling")
			fmt.Println(fmt.Sprint(err) + ": " + stderr.String())
		}
		completeCh <- time.Since(start)
	}
}

func session(requests, concurrency int, completeCh chan time.Duration) {
	var wg sync.WaitGroup
	n := requests / concurrency

	for i := 0; i < concurrency; i++ {
		wg.Add(1)
		go func() {
			worker(n, completeCh)
			wg.Done()
		}()
	}
	wg.Wait()
}

func bench(requests, concurrency int) {
	start := time.Now()

	timings := make([]float64, requests)
	// Create a buffered channel so our display goroutine can't slow down the workers.
	completeCh := make(chan time.Duration, requests)
	doneCh := make(chan struct{})
	current := 0
	go func() {
		for timing := range completeCh {
			timings = append(timings, timing.Seconds())
			current++
			percent := float64(current) / float64(requests) * 100
			fmt.Printf("[%3.f%%] %d/%d containers started\n", percent, current, requests)
		}
		doneCh <- struct{}{}
	}()
	session(requests, concurrency, completeCh)
	close(completeCh)
	<-doneCh

	total := time.Since(start)
	mean, _ := stats.Mean(timings)
	p90th, _ := stats.Percentile(timings, 90)
	p99th, _ := stats.Percentile(timings, 99)

	meanMillis := mean * MILLIS_IN_SECOND
	p90thMillis := p90th * MILLIS_IN_SECOND
	p99thMillis := p99th * MILLIS_IN_SECOND

	fmt.Printf("\n")
	fmt.Printf("Time taken for tests: %.3fs\n", total.Seconds())
	fmt.Printf("Time per container: %.3fms [mean] | %.3fms [90th] | %.3fms [99th]\n", meanMillis, p90thMillis, p99thMillis)
}

func main() {
	bench(30, 1)
}
