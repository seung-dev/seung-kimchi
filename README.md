# seung-kimchi

[![](https://jitpack.io/v/seung-dev/seung-kimchi.svg)](https://jitpack.io/#seung-dev/seung-kimchi)

### Build

```
$ cp settings.gradle.windows settings.gradle
$ vi settings.gradle
$ cp build.gradle.windows build.gradle
$ vi build.gradle
$ gradlew build
```

### Utilities

##### SText

- is_empty
- trim
- uuid
- random
- pad_right
- pad_left

##### SJson

- object_mapper
- stringify
- parse

##### SDate

- epoch
- format
- date
- diff
- add

##### SFormat

- encode_base64
- decode_base64
- encode_hex
- decode_hex
- compress
- decompress

##### SFile

- is_zip
- zip

##### SHttp

- nslookup
- encode_uri_component
- decode_uri
- content_disposition
- browser
- public_ip

##### SSecurity

- providers
- digest
- hmac
- encrypt
- decrypt
- keypair
- xxtea_decrypt
- xxtea_encrypt
- add_bouncy_castle_provider

##### SCertificate

- public_key
- private_key

### Publish

```cmd
winget install GnuPG.Gpg4win
```

```cmd
gpg --version
```

```cmd
gpg --full-generate-key
```

```cmd
gpg --list-secret-keys --keyid-format=long
```

```cmd
gpg --export --armor 79B325D975F5AF1C > seung.gpg.public.asc
```

```cmd
gpg --export-secret-keys --armor 79B325D975F5AF1C > seung.gpg.private.asc
```

```cmd
gpg --keyserver https://keys.openpgp.org --send-keys 79B325D975F5AF1C
```

```cmd
curl https://keys.openpgp.org/vks/v1/by-keyid/79B325D975F5AF1C
```


