# Notes

## Java 오픈소스 시작하기

### 0-0. Maven Central Repository 가입하기

[설명](https://central.sonatype.org/register/central-portal/#create-an-account)을 보시고 [가입](https://central.sonatype.org)하세요.

### 0-1. Maven Central Repository Namespace 추가하기

> [!TIP]
> 보유하고 있는 도메인이 없다면 Github 계정을 사용할 수 있습니다.
> 
> 예: Github: https://github.com/seung-dev &rarr; Namespace: io.github.seung-dev

### 0-2. Maven Central Repository 토큰 발급하기

[로그인](https://central.sonatype.com/) &rarr; 우측상단 프로필 &rarr; View Account &rarr; Generate User Token

```xml
<server>
	<id>${server}</id>
	<username>MAVEN_CENTRAL_USERNAME</username>
	<password>MAVEN_CENTRAL_PASSWORD</password>
</server>
```

> [!NOTE]
> 이 토큰의 username과 password는
> 
> gradle.properties 내 mavenCentralUsername과 mavenCentralPassword
> 
> 또는
> 
> Github &rarr; Repository &rarr; Settings &rarr; Secrets and variables &rarr; Actions &rarr; Secrets
> 
> 에 사용됩니다.

### 1-0. GPG(GNU Privacy Guard) 설치 및 버전 확인하기

```powershell
winget install GnuPG.Gpg4win
```

```powershell
gpg --version
```

### 1-1. GPG 키 생성 및 확인하기

```powershell
gpg --full-generate-key
```

```powershell
gpg --list-secret-keys --keyid-format=long
```

다음과 같은 결과에서 앞으로 사용하게될 아이디는 **0123456789ABCDEF** 입니다.

```
[keyboxd]
---------
sec   rsa4096/**0123456789ABCDEF** 2024-10-23 [SC]
      0123456789012345678901234567890123456789
uid                 [ultimate] Park Jong Seung <seung.dev@gmail.com>
ssb   rsa4096/7120E2B2B5353938 2024-10-23 [E]
```

제가 사용한 옵션입니다.

```
Please select what kind of key you want:
   (1) RSA and RSA
   (2) DSA and Elgamal
   (3) DSA (sign only)
   (4) RSA (sign only)
   (9) ECC (sign and encrypt) *default*
  (10) ECC (sign only)
  (14) Existing key from card
Your selection? 1
RSA keys may be between 1024 and 4096 bits long.
What keysize do you want? (3072) 4096
Requested keysize is 4096 bits
Please specify how long the key should be valid.
         0 = key does not expire
      <n>  = key expires in n days
      <n>w = key expires in n weeks
      <n>m = key expires in n months
      <n>y = key expires in n years
Key is valid for? (0) 0
Key does not expire at all
Is this correct? (y/N) y

GnuPG needs to construct a user ID to identify your key.

Real name: 홍길동
Email address: seung@gmail.com
Comment: 예제
```

### 1-2. GPG 키 내보내기

```powershell
gpg --export --armor 0123456789ABCDEF > seung.gpg.public.asc
```

```powershell
gpg --export-secret-keys --armor 0123456789ABCDEF > seung.gpg.private.asc
```

> [!CAUTION]
> 키가 분실될 경우를 대비해 백업하세요.

### 1-3. GPG 키 발행하기

키 서버 목록은 다음과 같으며 저는 keys.openpgp.org 를 사용했습니다.

- keyserver.ubuntu.com
- keys.openpgp.org
- pgp.mit.edu

```powershell
gpg --keyserver https://keys.openpgp.org --send-keys 0123456789ABCDEF
```

> [!WARNING]
> 아래 내용과 같은 메일을 수신하게 됩니다. 삭제 등 키 관리를 위해서 링크를 클릭하세요. 시간이 지나면 만료된 세션이라고 표시되면서 링크를 사용할 수 없습니다.
> 
> 만약 링크를 사용하지 못했으면 위에서 내보내기한 공개키를 선택하여 관리 하실 수 있습니다.
> 
> ```
> OpenPGP key: 0123456789012345678901234567890123456789
> This key was just uploaded for the first time, and is now published without identity information. If you want to allow others to find this key by e-mail address, please follow this link:
> https://keys.openpgp.org/upload/...
> ```

### 1-4. GPG 키 발행 확인하기

[홈페이지](https://keys.openpgp.org/search)

또는

```powershell
curl https://keys.openpgp.org/vks/v1/by-keyid/0123456789ABCDEF
```

### 2-0. Gradle 버전 업데이트

여기서는 [gradle-maven-publish-plugin](https://github.com/vanniktech/gradle-maven-publish-plugin) 를 사용합니다.

사용하게 될 [라이브러리](https://central.sonatype.org/publish/publish-portal-gradle/#alternatives)에 맞는 Gradle 버전으로 업데이트 하세요.

- ani2fun/sonatype-maven-central-publisher
- deepmedia/MavenDeployer
- GradleUp/nmcp
- Im-Fran/SonatypeCentralUpload
- lalakii/central-portal-plus
- pkmer/pkmerboot-central-publisher
- tddworks/central-portal-publisher
- thebugmc/sonatype-central-portal-publisher
- vanniktech/gradle-maven-publish-plugin
- yananhub/flying-gradle-plugin

```powershell
gradlew.bat wrapper --gradle-version latest
```

### 2-1. Gradle 설정하기

[가이드](https://vanniktech.github.io/gradle-maven-publish-plugin/central/)

gradle.properties

```
mavenCentralUsername=central.sonatype.org 에서 생성한 토큰 username
mavenCentralPassword=central.sonatype.org 에서 생성한 토큰 password
signingInMemoryKey=ASCII 형식으로 인코딩한 GPG 키. 예: lQdGBF4jUfwBEACblZV4uBViHcYLOb2280tEpr64iB9b6YRkWil3EODiiLd9JS3V...9pip+B1QLwEdLCEJA+3IIiw4qM5hnMw=
signingInMemoryKeyPassword=GPG 키를 만들때 작성한 비밀번호
```

> [!CAUTION]
> 인증정보를 gradle.properties 로 관리하려면 .gitignore 에 gradle.properties 를 꼭 추가하세요.

> [!IMPORTANT]
> signingInMemoryKey 에 설정한 키는 다음과 같은 명령어를 통해 ACSII 형식으로 인코딩합니다.
> 
> Windows:
>   ```
>   (gpg --export-secret-keys --armor 0123456789ABCDEF | Select-String -NotMatch '---|^=.' | Out-String) -replace '\s+', ''
>   ```
> Linux:
>   ```
>   gpg --export-secret-keys --armor 0123456789ABCDEF | grep -v '\-\-' | grep -v '^=.' | tr -d '\n'
>   ```

build.gradle

```
import com.vanniktech.maven.publish.SonatypeHost

plugins {
	id "java-library"
	id "com.vanniktech.maven.publish" version "0.30.0"
	id "signing"
}

def charset = "UTF-8"

group = "io.github.seung-dev"
archivesBaseName = project.name
version = "0.0.3"
description = "seung Library"

sourceCompatibility = JavaVersion.VERSION_17
targetCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	...
}

compileJava {
	options.encoding = charset
}

javadoc {
	options.encoding = charset
}

mavenPublishing {
	publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
	signAllPublications()
	coordinates(group, archivesBaseName, version)
	pom {
		name = archivesBaseName
		description = project.description
		url = "https://github.com/seung-dev/" + archivesBaseName
		licenses {
			license {
				name = "The Apache License, Version 2.0"
				url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
			}
		}
		developers {
			developer {
				id = "seung"
				name = "Park Jong Seung"
				email = "seung.dev@gmail.com"
			}
		}
		scm {
			connection = "scm:git:git://github.com/seung-dev/seung-kimchi.git"
			developerConnection = "scm:git:ssh://github.com/seung-dev/seung-kimchi.git"
			url = "https://github.com/seung-dev/seung-kimchi"
		}
	}
}

```

> [!IMPORTANT]
> 만약 이 글을 보시고 https://central.sonatype.com/ 에 가입했다면 SonatypeHost.CENTRAL_PORTAL 로 설정해야 합니다.

### 2-2. Gradle 빌드 테스트하기

```powershell
gradlew.bat clean build --refresh-dependencies --stacktrace
```

### 2-3. Gradle 릴리즈 테스트하기

```powershell
gradlew.bat publishAndReleaseToMavenCentral
```

### References

[https://central.sonatype.org/register/central-portal/](https://central.sonatype.org/register/central-portal/)

[https://vanniktech.github.io/gradle-maven-publish-plugin/](https://vanniktech.github.io/gradle-maven-publish-plugin/)

[https://github.com/vanniktech/gradle-maven-publish-plugin/tree/main](https://github.com/vanniktech/gradle-maven-publish-plugin/tree/main)

[https://docs.github.com/en/actions/use-cases-and-examples/publishing-packages/publishing-java-packages-with-gradle](https://docs.github.com/en/actions/use-cases-and-examples/publishing-packages/publishing-java-packages-with-gradle)

[https://simonscholz.dev/tutorials/publish-maven-central-gradle](https://simonscholz.dev/tutorials/publish-maven-central-gradle)
