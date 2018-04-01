@echo off

rem
rem コメント：WSDLソースコード生成ツール
rem

if "%~1"=="" (
  echo ERROR: パッケージを指定してください。
  echo USAGE: make.bat "パッケージ" "WSDLファイル"
  exit /b 1
)

if "%~2"=="" (
  echo ERROR: WSDLファイルを指定してください。
  echo USAGE: make.bat "パッケージ" "WSDLファイル"
  exit /b 1
)

wsimport -Xnocompile -extension -keep -b wsimport-config.xml -p %1 %2

echo OK: 生成完了。

exit /b 0
