#!/bin/bash

parcel build index.html --out-dir docs --log-level 4 --no-source-maps --public-url ./

if [ -d "../docs" ]; then
  rm -fr ../docs
fi

mkdir -p ../docs

cp -R docs/. ../docs/.
