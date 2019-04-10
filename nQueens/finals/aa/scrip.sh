#!/bin/sh
for name in *
do
    case "$name" in
    *.c)
        gcc -o $(basename "$name" .c) "$name"
        ;;
    *.cc)
        g++ -o $(basename "$name" .cc) "$name"
        ;;
    esac
done
