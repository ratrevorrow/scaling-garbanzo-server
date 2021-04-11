function bump {
  local mode="$1"
  local old="$2"
  local parts=( ${old//./ } )
  case "$1" in
    major)
      local bv=$((parts[0] + 1))
      NEW_VERSION="${bv}.0.0"
      ;;
    minor)
      local bv=$((parts[1] + 1))
      NEW_VERSION="${parts[0]}.${bv}.0"
      ;;
    patch)
      local bv=$((parts[2] + 1))
      NEW_VERSION="${parts[0]}.${parts[1]}.${bv}"
      ;;
    esac
}

OLD_VERSION=$(cat pom.xml | grep "<version>.*</version>" | sed -n 2p | awk -F'[><]' '{print $3}')
BUMP_MODE=$(git log | grep -E -- 'major|minor|patch' | sed -n 1p | cut -d'#' -f 2 | cut -d ' ' -f 1)

bump $BUMP_MODE $OLD_VERSION
echo "pom.xml will be bumped from" $OLD_VERSION "to" $NEW_VERSION
mvn versions:set -DnewVersion="${NEW_VERSION}"