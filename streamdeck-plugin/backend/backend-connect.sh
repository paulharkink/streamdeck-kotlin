#!/bin/bash
java -jar app.jar -backend http://localhost:8080/connect "$@" | tee backend.log
