#!/bin/bash

echo "=== DETAILED UNUSED FILES ANALYSIS ==="
echo "Categorizing potentially unused files by type and importance"
echo

# Categories for analysis
declare -a SAFE_TO_DELETE=()
declare -a FACTORIES_UNUSED=()
declare -a INTERFACES_UNUSED=() 
declare -a CONTROLLERS_UNUSED=()
declare -a DATA_ACCESS_UNUSED=()
declare -a VIEW_MODELS_UNUSED=()

find src/main/java -name "*.java" | while read -r file; do
    class_name=$(basename "$file" .java)
    
    # Skip test files and main methods
    if [[ "$file" == *"Test.java" ]] || grep -q "public static void main" "$file"; then
        continue
    fi
    
    # Check if file is referenced elsewhere
    usage_count=$(grep -r -l "$class_name" src/main/java --exclude="$(basename "$file")" | wc -l)
    
    if [[ $usage_count -eq 0 ]]; then
        line_count=$(wc -l < "$file")
        
        # Categorize the file
        case "$file" in
            *"Factory.java")
                echo "🏭 FACTORY: $file ($line_count lines)"
                ;;
            *"Controller.java")
                echo "🎮 CONTROLLER: $file ($line_count lines)"
                ;;
            *"ViewModel.java")
                echo "📱 VIEW MODEL: $file ($line_count lines)"
                ;;
            *"DataAccess"*|*"Repository.java"|*"Gateway.java")
                echo "💾 DATA ACCESS: $file ($line_count lines)"
                ;;
            *"Interface.java"|*"Boundary.java")
                echo "🔌 INTERFACE: $file ($line_count lines)"
                ;;
            *"Presenter.java")
                echo "🎭 PRESENTER: $file ($line_count lines)"
                ;;
            *"State.java")
                echo "📊 STATE: $file ($line_count lines)"
                ;;
            *)
                echo "❓ OTHER: $file ($line_count lines)"
                ;;
        esac
        
        # Quick preview of what the file contains
        if grep -q "interface " "$file"; then
            echo "   └── Interface definition"
        elif grep -q "abstract" "$file"; then
            echo "   └── Abstract class"
        elif [[ $line_count -lt 50 ]]; then
            echo "   └── Small file (< 50 lines) - likely safe to delete if truly unused"
        fi
        echo
    fi
done | head -50

echo
echo "=== QUICK DELETION CANDIDATES ==="
echo "Files that are likely safe to delete (small, unused, no main methods):"
echo

find src/main/java -name "*.java" -not -path "*/test/*" | while read -r file; do
    class_name=$(basename "$file" .java)
    
    if ! grep -q "public static void main" "$file" && \
       ! grep -q "@Test\|Test.*{" "$file"; then
        
        usage_count=$(grep -r -l "$class_name" src/main/java --exclude="$(basename "$file")" | wc -l)
        line_count=$(wc -l < "$file")
        
        if [[ $usage_count -eq 0 && $line_count -lt 100 ]]; then
            echo "🗑️  $file ($line_count lines)"
        fi
    fi
done | head -20