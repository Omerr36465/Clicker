# Ubuntu + Termux Build Fix

## Requirements

Inside Ubuntu on Termux:

```bash
apt update
apt install openjdk-17-jdk wget unzip -y
```

## Set Java 17

```bash
update-alternatives --config java
```

Choose Java 17.

Then:

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-arm64
export PATH=$JAVA_HOME/bin:$PATH
```

Verify:

```bash
java -version
```

Must show Java 17.

## Android SDK

Create local.properties:

```properties
sdk.dir=/root/android-sdk
```

## Restore Missing Gradle Wrapper JAR

```bash
mkdir -p gradle/wrapper
wget https://raw.githubusercontent.com/gradle/gradle/master/gradle/wrapper/gradle-wrapper.jar -O gradle/wrapper/gradle-wrapper.jar
chmod +x gradlew
```

## Build APK

```bash
./gradlew clean
./gradlew assembleDebug
```

APK output:

```bash
app/build/outputs/apk/debug/app-debug.apk
```
