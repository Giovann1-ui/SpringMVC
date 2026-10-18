#!/bin/bash

APP_NAME="framework"

SRC_DIR="./src"
BUILD_DIR="./bin"
LIB_DIR="./lib"

echo "================================"
echo " BUILD FRAMEWORK"
echo "================================"

echo ""
echo "[1] Nettoyage..."
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

echo ""
echo "[2] Recherche des sources..."

find "$SRC_DIR" -name "*.java" > sources.txt

if [ ! -s sources.txt ]; then
    echo "❌ Aucun fichier Java trouvé dans $SRC_DIR"
    rm -f sources.txt
    exit 1
fi

echo ""
echo "[3] Compilation..."

javac \
-cp "$LIB_DIR/*" \
-d "$BUILD_DIR" \
@sources.txt

if [ $? -ne 0 ]; then
    echo ""
    echo "❌ Compilation échouée"
    rm -f sources.txt
    exit 1
fi

rm -f sources.txt

echo "✅ Compilation OK"

echo ""
echo "[4] Création du JAR..."

jar cf "$APP_NAME.jar" -C "$BUILD_DIR" .

if [ $? -ne 0 ]; then
    echo "❌ Erreur création JAR"
    exit 1
fi

echo ""
echo "================================"
echo "✅ FRAMEWORK CONSTRUIT"
echo "================================"

echo ""
echo "Fichier généré :"
echo "./$APP_NAME.jar"

echo ""
echo "[5] Copie vers WEB-INF/lib..."

WEB_INF_LIB="../WEB-INF/lib"

if [ ! -d "$WEB_INF_LIB" ]; then
    echo "❌ Dossier $WEB_INF_LIB introuvable"
    exit 1
fi

cp "$APP_NAME.jar" "$WEB_INF_LIB/"

echo "✅ Copié dans $WEB_INF_LIB"
ls -la "$WEB_INF_LIB"
