# 部活のお題出すやつ

## 使い方

1. [ダウンロード](https://github.com/foretnoire666/chooseRandomClub/releases/download/latest/chooseRandomClub.zip)
2. zipを解答
3. `chooseRandomClub.exe`をダブルクリックで実行
- 初回起動時
   1. お題となる部活名が表示されます
       ```shell
       「部活名」を描いて下さい
       ```
   2. 表示されるとappフォルダに下記ファイルが作成されます
      - `allClubs.txt`: お題として出された部活以外の全部活のリスト
      - `currentClub.txt`: 今お題として出されている部活名
      - `drawnClubs.tsv`: 描いた部活と生徒名のリスト(ヘッダ付きtsv)
- 2回目以降
  1. 描き終えたか確認が入るので描き終えていたら`y`描き終えていなかったら`n`を押下  
     nを押下した場合お題は出されずに終了します
  2. `y`を押下した場合描いた生徒名を聞かれるので描いた生徒を入力して下さい
      ```shell
      誰を描きましたか？
      ```
  3. `drawnClubs.tsv`に描いた内容が記録され、新しいお題が出されます
3. 終了確認が出ます`。y`を押下で終了します
```shell
終了するにはyを押してください
```

## FAQ

- 部活が追加されたので追加したい
  - `allClubs.txt`に追加して下さい
- 描いた生徒名を間違えた
  - `drawnClubs.tsv`を修正してください
- なんでコミカライズオリジナルの不動産管理仲介部もあるの？
  - 不動アンを描いてください

## ライセンス

```text
Copyright © 2026 foretnoire666 <gato.e.crescente@gmail.com>
This work is free. You can redistribute it and/or modify it under the
terms of the Do What The Fuck You Want To Public License, Version 2,
as published by Sam Hocevar. See the COPYING file for more details.
```