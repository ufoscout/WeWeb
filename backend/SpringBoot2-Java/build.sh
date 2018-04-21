
echo $LINE_SEPARATOR
echo 'Build SpringBoot2-Java'
echo $LINE_SEPARATOR

mvn clean verify
rc=$?
if [[ $rc -ne 0 ]] ; then
  echo 'Failure'; exit $rc
fi



