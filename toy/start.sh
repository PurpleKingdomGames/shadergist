#!/bin/bash

cp ../toy-game/target/scala-3.0.0/indigo-toy-fastopt.js .

parcel index.html --no-cache --out-dir dist --log-level 4 --no-source-maps
