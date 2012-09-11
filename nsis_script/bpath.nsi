; example2.nsi
;
; This script is based on example1.nsi, but it remember the directory, 
; has uninstall support and (optionally) installs start menu shortcuts.
;
; It will install example2.nsi into a directory that the user selects,

;--------------------------------

; The name of the installer
Name "Bit Pathways"

; The file to write
OutFile "bpath_install.exe"

; Icon
Icon "${NSISDIR}\Contrib\Graphics\Icons\nsis1-install.ico"

; The default installation directory
InstallDir $PROGRAMFILES\BitPathways

; Registry key to check for directory (so if you install again, it will 
; overwrite the old one automatically)
InstallDirRegKey HKLM "Software\BitPathways" "Install_Dir"

; Request application privileges for Windows Vista
RequestExecutionLevel admin

LicenseText "License agreement for Bit Pathways 2.1"
LicenseData "license.rtf"
LicenseForceSelection radiobuttons "I accept" "I decline"


;--------------------------------

; Pages

Page license
Page components
Page directory
Page instfiles

UninstPage uninstConfirm
UninstPage instfiles

;--------------------------------

; The stuff to install
Section "Bit Pathways (required)"

  SectionIn RO
  
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  
  ; Put file there
  File "..\bin\bp.bat"
  File "..\bin\bp.jar"
  File "..\bin\bp_pl.bat"
  
  File /r "..\bin\lib"
  File /r "..\bin\conf"
  File /r "..\bin\resources"
  
  
  ; Write the installation path into the registry
  WriteRegStr HKLM SOFTWARE\BitPathways "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BitPathways" "DisplayName" "Bit Pathways"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BitPathways" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BitPathways" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BitPathways" "NoRepair" 1
  WriteUninstaller "uninstall.exe"
  
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu & Desktop Shortcuts"

  CreateDirectory "$SMPROGRAMS\Bit Pathways"
  CreateShortCut "$SMPROGRAMS\Bit Pathways\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe" 0
  CreateShortCut "$SMPROGRAMS\Bit Pathways\Bit Pathways.lnk" "$INSTDIR\bp.bat" "" "$INSTDIR\bp.bat" 0
  CreateShortCut "$DESKTOP\Bit Pathways.lnk" "$INSTDIR\bp.bat" "" "$INSTDIR\bp.bat" 0
  
SectionEnd

;--------------------------------

; Uninstaller

Section "Uninstall"
  
  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\BitPathways"
  DeleteRegKey HKLM SOFTWARE\BitPathways

  ; Remove files and uninstaller
  Delete $INSTDIR\bp.bat
  Delete $INSTDIR\bp.jar
  Delete $INSTDIR\bp_pl.bat
  Delete $INSTDIR\uninstall.exe
  Delete $INSTDIR\lib\*.*
  Delete $INSTDIR\conf\*.*
  Delete $INSTDIR\resources\*.*  
  
  ; Remove shortcuts, if any
  Delete "$SMPROGRAMS\Bit Pathways\*.*"
  Delete "$DESKTOP\Bit Pathways.lnk"

  ; Remove directories used
  RMDir "$SMPROGRAMS\Bit Pathways"
  RMDir "$INSTDIR\lib"
  RMDir "$INSTDIR\conf"
  RMDir "$INSTDIR\resources"  
  RMDir "$INSTDIR"

SectionEnd
