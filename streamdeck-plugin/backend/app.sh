#!/bin/bash
java -jar app.jar "$@" | tee backend.log
