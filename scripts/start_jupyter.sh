#!/bin/sh

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
jupyter notebook --notebook-dir="$SCRIPT_DIR/../strategy"
