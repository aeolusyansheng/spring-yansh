@echo off

rem
rem �R�����g�FWSDL�\�[�X�R�[�h�����c�[��
rem

if "%~1"=="" (
  echo ERROR: �p�b�P�[�W���w�肵�Ă��������B
  echo USAGE: make.bat "�p�b�P�[�W" "WSDL�t�@�C��"
  exit /b 1
)

if "%~2"=="" (
  echo ERROR: WSDL�t�@�C�����w�肵�Ă��������B
  echo USAGE: make.bat "�p�b�P�[�W" "WSDL�t�@�C��"
  exit /b 1
)

wsimport -Xnocompile -extension -keep -b wsimport-config.xml -p %1 %2

echo OK: ���������B

exit /b 0
