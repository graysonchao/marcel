#!/bin/bash

install() {
    sh -c "$(curl -fsSL https://raw.githubusercontent.com/HaliteChallenge/Halite/master/environment/install.sh)"
    mv halite bin/
}

cleanup() {
    rm -rf build dist *egg-info
}

install
python setup.py install
cleanup
