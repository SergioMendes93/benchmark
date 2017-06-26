FROM openjdk:7
COPY . /home/sergiomendes/benchmark
WORKDIR /home/sergiomendes/benchmark
ENV port 8000
RUN javac timeServer.java
ENTRYPOINT ["java","timeServer"]
#CMD ["java","timeServer", "$port"]
CMD ["args"]


