# Notes

## Java 오픈소스 시작하기

### Maven Central Repository

##### 가입

[가이드](https://central.sonatype.org/register/central-portal/#create-an-account)

##### Namespace 추가

> [!TIP]
> 보유하고 있는 도메인이 없다면 Github 계정을 사용하세요.
> 예:
>   Github: https://github.com/seung-dev
>   Namespace: io.github.seung-dev

##### 토큰 발급

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
> gradle.properties 내 mavenCentralUsername과 mavenCentralPassword
> 또는
> Github &rarr; Repository &rarr; Settings &rarr; Secrets and variables &rarr; Actions &rarr; Secrets
> 에 사용됩니다.

### GNU Privacy Guard

##### 설치 및 버전 확인

```powershell
winget install GnuPG.Gpg4win
```

```powershell
gpg --version
```

##### 키 생성 및 확인

- kind: (1) RSA and RSA
- size: 4096
- expires: 0

```powershell
gpg --full-generate-key
```

```powershell
gpg --list-secret-keys --keyid-format=long
```

> [!NOTE]
> 다음과 같은 결과에서 앞으로 사용하게될 아이디는 **0123456789ABCDEF** 입니다.
> 
> [keyboxd]
> ---------
> sec   rsa4096/**0123456789ABCDEF** 2024-10-23 [SC]
>       0123456789012345678901234567890123456789
> uid                 [ultimate] Park Jong Seung <seung.dev@gmail.com>
> ssb   rsa4096/7120E2B2B5353938 2024-10-23 [E]

> [!TIP]
> 테스트 중이시면 아래 제가 선택한 옵션을 참고하세요.
> 
> Please select what kind of key you want:
>    (1) RSA and RSA
>    (2) DSA and Elgamal
>    (3) DSA (sign only)
>    (4) RSA (sign only)
>    (9) ECC (sign and encrypt) *default*
>   (10) ECC (sign only)
>   (14) Existing key from card
> Your selection? 1
> RSA keys may be between 1024 and 4096 bits long.
> What keysize do you want? (3072) 4096
> Requested keysize is 4096 bits
> Please specify how long the key should be valid.
>          0 = key does not expire
>       <n>  = key expires in n days
>       <n>w = key expires in n weeks
>       <n>m = key expires in n months
>       <n>y = key expires in n years
> Key is valid for? (0) 0
> Key does not expire at all
> Is this correct? (y/N) y
> 
> GnuPG needs to construct a user ID to identify your key.
> 
> Real name: 홍길동
> Email address: seung@gmail.com
> Comment: 예제

##### 키 내보내기

```powershell
gpg --export --armor 0123456789ABCDEF > seung.gpg.public.asc
```

```powershell
gpg --export-secret-keys --armor 0123456789ABCDEF > seung.gpg.private.asc
```

> [!CAUTION]
> 키가 분실될 경우를 대비해 백업하세요.

##### 키 발행

키 서버 목록은 다음과 같으며 어디든 등록해도 됩니다. 저는 openpgp로 작성하였습니다.

- keyserver.ubuntu.com
- keys.openpgp.org
- pgp.mit.edu

```powershell
gpg --keyserver https://keys.openpgp.org --send-keys 0123456789ABCDEF
```

> [!WARNING]
> 아래 내용과 같은 메일을 수신할 수 있습니다. 삭제 등 키 관리를 위해서 링크를 클릭하세요. 시간이 지나면 만료된 세션이라고 표시되면서 링크를 사용할 수 없습니다.
> 만약 링크를 사용하지 못했으면 위에서 내보내기한 공개키를 선택하여 관리 하실 수 있습니다.
> 
> OpenPGP key: 0123456789012345678901234567890123456789
> This key was just uploaded for the first time, and is now published without identity information. If you want to allow others to find this key by e-mail address, please follow this link:
> https://keys.openpgp.org/upload/...

##### 발행 확인

[홈페이지](https://keys.openpgp.org/search)

또는

```powershell
curl https://keys.openpgp.org/vks/v1/by-keyid/79B325D975F5AF1C
```

### Gradle

##### 버전 업데이트

사용하게 될 라이브러리에 맞는 Gradle 를 업데이트 하세요.

##### gradle.properties 및 build.gradle 설정

[가이드](https://vanniktech.github.io/gradle-maven-publish-plugin/central/)

gradle.properties

```
GROUP_ID=central.sonatype.org 에서 생성한 Namespace
VERSION=버전
DESCRIPTION=설명
CHARSET=UTF-8
mavenCentralUsername=central.sonatype.org 에서 생성한 토큰 username
mavenCentralPassword=central.sonatype.org 에서 생성한 토큰 password
signingInMemoryKey=ASCII 형식으로 인코딩한 GPG 키. 예: lQdGBF4jUfwBEACblZV4uBViHcYLOb2280tEpr64iB9b6YRkWil3EODiiLd9JS3V...9pip+B1QLwEdLCEJA+3IIiw4qM5hnMw=
signingInMemoryKeyPassword=GPG 키를 만들때 작성한 비밀번호
```

> [!CAUTION]
> 인증정보를 gradle.properties 로 관리하려면 .gitignore 에 gradle.properties 를 꼭 추가하세요.

> [!IMPORTANT]
> signingInMemoryKey 에 설정한 키정보는 다음과 같은 명령어를 통해 확인하세요.
> 
> Windows:
>   (gpg --export-secret-keys --armor 0123456789ABCDEF | Select-String -NotMatch '---|^=.' | Out-String) -replace '\s+', ''
> Linux:
>   gpg --export-secret-keys --armor 0123456789ABCDEF | grep -v '\-\-' | grep -v '^=.' | tr -d '\n'

build.gradle

```
import com.vanniktech.maven.publish.SonatypeHost

plugins {
	id "java-library"
	id "com.vanniktech.maven.publish" version "0.30.0"
	id "signing"
}

def g = findProperty("GROUP_ID") ?: (System.getenv("GROUP_ID") ?: "io.github.seung-dev")
def n = project.name
def v = findProperty("VERSION") ?: (System.getenv("VERSION") ?: "0.0.0")
def d = findProperty("DESCRIPTION") ?: (System.getenv("DESCRIPTION") ?: "0.0.0")
def c = findProperty("CHARSET") ?: (System.getenv("CHARSET") ?: "UTF-8")

group = g
version = v

sourceCompatibility = JavaVersion.VERSION_17
targetCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	...
}

compileJava {
	options.encoding = c
}

javadoc {
	options.encoding = c
}

mavenPublishing {
	publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
	signAllPublications()
	coordinates(g, n, v)
	pom {
		name = n
		description = d
		url = "https://github.com/seung-dev/" + n
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

##### 빌드 테스트

```powershell
gradlew.bat clean build --refresh-dependencies --stacktrace
```

##### 릴리즈 테스트

```powershell
gradlew.bat publishAndReleaseToMavenCentral
```

### References

[https://central.sonatype.org/register/central-portal/](https://central.sonatype.org/register/central-portal/)

[https://vanniktech.github.io/gradle-maven-publish-plugin/](https://vanniktech.github.io/gradle-maven-publish-plugin/)

[https://github.com/vanniktech/gradle-maven-publish-plugin/tree/main](https://github.com/vanniktech/gradle-maven-publish-plugin/tree/main)

[https://docs.github.com/en/actions/use-cases-and-examples/publishing-packages/publishing-java-packages-with-gradle](https://docs.github.com/en/actions/use-cases-and-examples/publishing-packages/publishing-java-packages-with-gradle)

[https://simonscholz.dev/tutorials/publish-maven-central-gradle](https://simonscholz.dev/tutorials/publish-maven-central-gradle)
