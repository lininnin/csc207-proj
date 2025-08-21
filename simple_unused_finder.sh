#!/bin/bash

echo "=== SIMPLE UNUSED FILE FINDER ==="
echo "Looking for Java files that are never imported by other files..."
echo

# Find all Java files and extract class names
find src/main/java -name "*.java" | while read -r file; do
    # Get the class name from file path
    class_name=$(basename "$file" .java)
    
    # Skip if it's a test file
    if [[ "$file" == *"Test.java" ]]; then
        continue
    fi
    
    # Skip if it has main method (entry points)
    if grep -q "public static void main" "$file"; then
        continue
    fi
    
    # Count how many files import or reference this class
    usage_count=$(grep -r "$class_name" src/main/java --exclude="$(basename "$file")" | wc -l)
    
    if [[ $usage_count -eq 0 ]]; then
        line_count=$(wc -l < "$file")
        echo "$file ($line_count lines) - NOT REFERENCED"
        
        # Show package
        package=$(head -5 "$file" | grep "^package " | sed 's/package //g; s/;//g' | tr -d ' ')
        if [[ -n "$package" ]]; then
            echo "  Package: $package"
        fi
        
        # Show class type
        if grep -q "interface " "$file"; then
            echo "  Type: Interface"
        elif grep -q "abstract class" "$file"; then
            echo "  Type: Abstract Class"
        elif grep -q "enum " "$file"; then
            echo "  Type: Enum"
        else
            echo "  Type: Class"
        fi
        echo
    fi
done