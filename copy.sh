#!/bin/bash

# Folder to read
SRC_DIR="./src"

# Output file
OUTPUT_FILE="all_files.txt"

# Empty the output file if it exists
> "$OUTPUT_FILE"

# Recursively read all files and append their contents
find "$SRC_DIR" -type f | while read file; do
    echo "===== $file =====" >> "$OUTPUT_FILE"
    cat "$file" >> "$OUTPUT_FILE"
    echo -e "\n\n" >> "$OUTPUT_FILE"
done

echo "All files concatenated into $OUTPUT_FILE"
