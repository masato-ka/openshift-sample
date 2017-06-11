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

以下のコマンドでminishiftを起動します。環境によって--vm-driverオプションやproxyの設定が必要になります。
詳細は[このページを参照してください。](https://docs.openshift.org/latest/minishift/index.html)

'''
$minishift start
'''

ocコマンド(OpenShift client)へパスを通します。
今回はOpenShift 1.5.0を使いました。バージョン表記は環境に合わせて変更してください。

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

[参考](https://docs.openshift.org/latest/using_images/db_images/postgresql.html)

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
$ oc new-app -e SPRING_PROFILES_ACTIVE=production -e POSTGRESQL_USER=user -e POSTGRESQL_PASSWORD=password -e POSTGRESQL_DATABASE=sample codecentric/springboot-maven3-centos~https://github.com/masato-ka/openshift-sample.git
```

コマンドは以下の書式になっています。
```
$ oc new-app -e SPRING_PROFILES_ACTIVE=Springのプロファイル名 -e POSTGRESQL_USER=データベースユーザ -e POSTGRESQL_PASSWORD=パスワード -e POSTGRESQL_DATABASE=データベース名 アプリケーションを動かすコンテナイメージ~アプリケーションのリポジトリURL
```

ビルド中のログは以下のコマンド確認できます。

```
$ oc log -f bc/openshift-sample
```

ビルド完了後、アプリケーションのログは以下のコマンドで確認できます。

```
$ oc log -f dc/openshift-sample
```

### URLのexpose

下記のコマンドで外部にアプリケーションを公開します。

```
$oc expose svc/openshift-sample
```

公開されるホスト名は以下のコマンドで取得できます。

```
$ oc get route
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

