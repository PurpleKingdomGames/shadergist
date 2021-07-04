#!/bin/bash

parcel build index.html --out-dir docs --log-level 4 --no-source-maps

if [ -d "../docs" ]; then
  rm -fr ../docs
fi

mkdir -p ../docs

cp -R docs/. ../docs/.
