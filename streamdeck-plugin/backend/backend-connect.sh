#!/bin/bash

original_args=("$@")

while [[ $# -gt 0 ]]; do
  key="$1"

  case $key in
  -port)
    port="$2"
    shift
    shift
    ;;
  -pluginUUID)
    pluginUUID="$2"
    shift
    shift
    ;;
  -registerEvent)
    registerEvent="$2"
    shift
    shift
    ;;
  -info)
    info="$2"
    shift
    shift
    ;;
  *)
    echo "Unknown option: $key"
    exit 1
    ;;
  esac
done

# Validate that all required parameters are set
if [[ -z "$port" || -z "$pluginUUID" || -z "$registerEvent" || -z "$info" ]]; then
  echo "One or more required parameters are missing."
  exit 1
fi

(
#  echo "Script was called with: $original_args"
#  for arg in "${original_args[@]}"; do
#    echo "[$arg]"
#  done
#
#  echo "Port: $port"
#  echo "Plugin UUID: $pluginUUID"
#  echo "Register Event: $registerEvent"
#  echo "Info: $info"

  echo "Contacting running plugin backend to connect to StreamDeck"

# pluginUUID=\"$pluginUUID\" registerEvent=\"$registerEvent\"
  set -x
#  escaped_info=$(echo "$info" | sed 's/"/\\"/g')

  httpie_arguments=(-v POST localhost:8080/connect port="$port")
  httpie_arguments+=("info:=$info")
  http "${httpie_arguments[@]}"

  set +x


#  set -x
#  http -v POST localhost:8080/connect port="$port" pluginUUID="$pluginUUID" registerEvent="$registerEvent" info:=$info
#
#  set +x
  echo "My job is done."
) >>backend.log 2>&1
