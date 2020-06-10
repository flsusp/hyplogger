# Instruction on how to release new versions

```
mvn versions:set -DnewVersion=${version}
git add .
git commit -m 'v${version}'
git push
mvn clean deploy

mvn versions:set -DnewVersion=${new-version}
git add .
git commit -m 'v${new-version}'
git push
```
