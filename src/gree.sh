#!/bin/bash
pool=(1 2 3 4 5 6 7 8 9 10)

roundPool(){
  num=${#pool[*]}
  result=${pool[$((RANDOM%num))]}
  return $result
}

date(){
  date -s
}

roundPool

echo $?

