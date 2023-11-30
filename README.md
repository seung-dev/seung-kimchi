# seung-kimchi
Java Utilities

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

##### SConvert

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
