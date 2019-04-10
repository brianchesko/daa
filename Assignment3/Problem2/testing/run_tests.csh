#!/usr/bin/env bash

declare -a input_suffixes=("1" "2" "3"
                "4" "5" "6"
                "7" "7_1" "7_2")
testOutputPrefix="output"
timeOutputPrefix="time"
inputPrefix="input"
testToBin="../bin/"
binToTest="../testing/"
driverName="Driver"
currDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$currDir/$testToBin"
for i in "${input_suffixes[@]}"
do
    echo "running test for input$i"
    (time java "$driverName" < "$binToTest$inputPrefix$i" > "$binToTest$testOutputPrefix$i") 2> "$binToTest$timeOutputPrefix$i"
done
