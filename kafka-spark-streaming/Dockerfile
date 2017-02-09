FROM hseeberger/scala-sbt
RUN mkdir /build
WORKDIR /build
ADD build.sbt /build
ADD project/assembly.sbt /build/project/assembly.sbt
RUN sbt update
ADD . /build
RUN sbt compile
# RUN sbt assembly
ENTRYPOINT ["sbt"]
