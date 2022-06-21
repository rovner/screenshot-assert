1. Publish
```
./gradlew clean build -x test :screenshot-assert-core:publish :screenshot-assert-junit5:publish
```
2. Go to [sonatype](https://s01.oss.sonatype.org/#stagingRepositories), select repository, hit close, hit release