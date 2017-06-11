# Sample program SpringBoot for OpenShift



SpringBootのアプリケーションをOpenShiftにデプロイするためのサンプルプログラムです。
OpenShift上でPostgreSQLのコンテナと接続して利用します。
ローカル環境ではPostgreSQLの代わりにH2 DBを利用して起動します。

## Getting Started

OpenShift環境はminishiftを利用してローカルに構築します。
minishiftのインストール方法は[Installing minishift](https://docs.openshift.org/latest/minishift/getting-started/installing.html)を参考にしてください。

ハイパーバイザーにOSXのxhyveとLinuxのKVMを利用する場合、Docker Machineのドライバーをインストールする必要があります。
[Setting up the plugin driver](https://docs.openshift.org/latest/minishift/getting-started/setting-up-driver-plugin.html)

### StartUp minishift

以下のコマンドでminishiftを起動します。

'''
# 各OSデフォルトのHyperVisorを使う場合
$minishift start
'''

ocコマンド(OpenShift client)へパスを通します。
'''
$ export PATH=$PATH;~/.minishift/cache/oc/v1.5.0/
'''

以上でOpenShiftの準備は完了です。

### Preparing project

以下のコマンドで新しいプロジェクトを作成します。プロジェクト名は任意です。
ここではspring-boot-sampleとしています。

```
$ oc new-project spring-boot-sample
```

### Create PostgreSQL Container

PostgreSQLのコンテナをspring-boot-sampleプロジェクトへ作成します。
アプリケーションから接続するユーザ名、パスワード、使用するDB名を指定します。
それぞれの値はコンテナの環境変数として指定します。

| ENV Name   | The specified value | Meaning |
|:-----------|--------------------:|:-------:|
| POSTGRESQL_USER       |        user |     接続ユーザ名     |
| POSTGRESQL_PASSWORD     |      password |    パスワード    |
| POSTGRESQL_DATABASE       |        sample |     データベース名    |

```
$ oc new-app -e POSTGRESQL_USER=user -e POSTGRESQL_PASSWORD=password -e POSTGRESQL_DATABASE=sample centos/postgresql-94-centos7
```

### Deploy sample application

PostgreSQLのコンテナが起動した後、アプリケーションのデプロイを実行します。
アプリケーションのデプロイは以下のコマンドで実行します。

```
$ oc new-app codecentric/springboot-maven3-centos~https://github.com/masato-ka/openshift-sample.git -e POSTGRES_URL=postgres://user:password@172.30.54.151/sample
```
new-app直後にこのアプリケーションを動かすDocker Imageの名前を指定します。'~'に続けてアプリケーションのリポジトリURLを指定します。
最後に環境変数名を指定します。

| ENV Name   | The specified value | Meaning |
|:-----------|--------------------:|:-------:|
| POSTGRESQL_URL       | postgres://user:password@xxx.xxx.xxx.xxx:5432/sample |    postgresの指定値を変更　     |
| SPRING_PROFILES_ACTIVE     |   production |    Springのプロファイルを指定。今回はapplication.ymlの読み込みを変更    |

### URLのexpose

下記のコマンドを使い外部にアプリケーションを公開する。

```
$oc expose openshift-sample
```


## Usage

### Access to the application

* Top page

```
$curl hostname
```

* POST the data

```
$curl -v -H "Content-Type: application/json" -X POST http://[Domain your application]/api/v1/books -d "{\"bookName\":\"hoge\", \"isbnCode\":\"123456789\", \"price\": 1200}" | jq
```

* GET Data

```
$curl -v -X GET http://[Domain your application]/api/v1/books | jq
```


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

