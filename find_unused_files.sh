#!/bin/bash

# Script to find potentially unused Java files in the project
# This analyzes import statements and cross-references them

echo "=== FINDING POTENTIALLY UNUSED FILES ==="
echo

# Create temporary files for analysis
TEMP_DIR=$(mktemp -d)
ALL_JAVA_FILES="$TEMP_DIR/all_java_files.txt"
ALL_IMPORTS="$TEMP_DIR/all_imports.txt"
ALL_CLASSES="$TEMP_DIR/all_classes.txt"
REFERENCED_CLASSES="$TEMP_DIR/referenced_classes.txt"
UNUSED_CANDIDATES="$TEMP_DIR/unused_candidates.txt"

echo "Analyzing 547 Java files..."

# Step 1: Find all Java files
find src/main/java -name "*.java" > "$ALL_JAVA_FILES"

# Step 2: Extract all import statements (remove 'import ' and ';')
echo "Extracting imports..."
find src/main/java -name "*.java" -exec grep -h "^import " {} \; | \
    sed 's/import //g; s/;//g; s/static //g' | \
    grep -v "java\." | \
    grep -v "javax\." | \
    grep -v "org\." | \
    sort | uniq > "$ALL_IMPORTS"

# Step 3: Extract all class names from file paths
echo "Extracting class names..."
cat "$ALL_JAVA_FILES" | \
    sed 's|src/main/java/||g; s|/|.|g; s|\.java||g' | \
    sort > "$ALL_CLASSES"

# Step 4: Find classes that are imported/referenced
echo "Finding referenced classes..."
cat "$ALL_IMPORTS" > "$REFERENCED_CLASSES"

# Also find classes referenced in code (new keyword, extends, implements)
find src/main/java -name "*.java" -exec grep -h "new [A-Z]" {} \; | \
    sed 's/.*new \([A-Z][A-Za-z0-9_]*\).*/\1/g' >> "$REFERENCED_CLASSES"

find src/main/java -name "*.java" -exec grep -h "extends [A-Z]" {} \; | \
    sed 's/.*extends \([A-Z][A-Za-z0-9_]*\).*/\1/g' >> "$REFERENCED_CLASSES"

find src/main/java -name "*.java" -exec grep -h "implements [A-Z]" {} \; | \
    sed 's/.*implements \([A-Z][A-Za-z0-9_]*\).*/\1/g' >> "$REFERENCED_CLASSES"

sort "$REFERENCED_CLASSES" | uniq > "$REFERENCED_CLASSES.sorted"
mv "$REFERENCED_CLASSES.sorted" "$REFERENCED_CLASSES"

# Step 5: Find potentially unused files
echo "Finding unused candidates..."
comm -23 "$ALL_CLASSES" "$REFERENCED_CLASSES" > "$UNUSED_CANDIDATES"

echo "=== RESULTS ==="
echo
echo "Total Java files: $(wc -l < "$ALL_JAVA_FILES")"
echo "Total imports found: $(wc -l < "$ALL_IMPORTS")"
echo "Potentially unused files: $(wc -l < "$UNUSED_CANDIDATES")"
echo

echo "=== POTENTIALLY UNUSED FILES ==="
echo "Files that don't appear to be imported or directly referenced:"
echo

# Convert back to file paths and show details
while read -r class; do
    file_path="src/main/java/${class//./\/}.java"
    if [[ -f "$file_path" ]]; then
        # Get file size and basic info
        size=$(wc -l < "$file_path" 2>/dev/null || echo "0")
        echo "$file_path ($size lines)"
        
        # Show what package it's in
        package=$(head -5 "$file_path" | grep "^package " | sed 's/package //g; s/;//g')
        if [[ -n "$package" ]]; then
            echo "  └── Package: $package"
        fi
        
        # Check if it's a main method or test
        if grep -q "public static void main" "$file_path"; then
            echo "  └── HAS MAIN METHOD - might be entry point!"
        fi
        
        if grep -q "@Test\|class.*Test" "$file_path"; then
            echo "  └── TEST FILE - keep for testing"
        fi
        echo
    fi
done < "$UNUSED_CANDIDATES"

echo "=== MANUAL VERIFICATION NEEDED ==="
echo "Before deleting any files, manually verify:"
echo "1. Files with main() methods might be entry points"
echo "2. Test files should be kept"
echo "3. Files might be used via reflection or annotations"
echo "4. Files might be used in configuration/XML files"
echo "5. Check git history to see if files are actively developed"
echo

# Cleanup
rm -rf "$TEMP_DIR"

echo "=== ALTERNATIVE: Check with Git ==="
echo "To find files not modified recently:"
echo "git log --name-only --since='3 months ago' --pretty=format: | sort | uniq > recently_modified.txt"
echo "Then compare all files against recently_modified.txt"