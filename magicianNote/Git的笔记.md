# Git 笔记

[TOC]
## 参考资料

* [简单对比git pull和git pull --rebase的使用](https://www.cnblogs.com/kevingrace/p/5896706.html)

* [git rebase的用法](https://blog.csdn.net/TTKatrina/article/details/79288238)

  

## n.随记

> git checkout - - file命令中的- -很重要，没有- -，就变成了“切换到另一个分支”的命令
>**`：wq`**可用来退出编辑界面

##m.git常用命令
>Git基本常用命令如下：
>　　**`mkdir`**：         XX (创建一个空目录 XX指目录名)
>　　**`cd`**:                移动到该路径
>　　**`pwd`**：          显示当前目录的路径。
>　　**`git init`**         把当前的目录变成可以管理的git仓库，生成隐藏.git文件。
>　　**`git add XX`**       把xx文件添加到暂存区去。
>　　**`git commit –m “XX`**”  提交文件 –m 后面的是注释。
>　　**`git status `**       查看仓库状态
>　　**`git diff  XX`**      查看XX文件修改了那些内容
>　　**`git log `**     查看历史记录 (缩写为**`git lg`**)
>　　**`git reset  --hard HEAD^ `**或者 **`git reset  --hard HEAD~ `**回退到上一个版本
>　　(如果想回退到100个版本，使用git reset –hard HEAD~100 )
>　　**`cat XX `**        查看XX文件内容
>　　**`git reflog `**      查看历史记录的版本号id
>　　**`git checkout -- XX `** 把XX文件在工作区的修改全部撤销。
>　　**`git rm XX`**          删除XX文件
>　　**`git remote add origin https://github.com/tugenhua0707/testgit`** 关联一个远程库
>　　**`git push –u(第一次要用-u 以后不需要) origin master `**把当前master分支推送到远程库
>　　**`git clone https://github.com/tugenhua0707/testgit `** 从远程库中克隆
>       **`git clone https://github.com/tugenhua0707/testgit--depth=1 `** 浅复制，只下载最近一次的版本
>​      **`git clone -b <branch name> [remote repository address] `** 克隆某一个特定的远程分支
>　　**`git fetch --unshallow`** 获取完整历史信息
>　　**`git checkout –b dev`**  创建dev分支 并切换到dev分支上
>　　**`git branch `** 查看当前所有的分支
>　　**`git checkout master`** 切换回master分支
>　　**`git merge dev`**    在当前的分支上合并dev分支
>　　**`git branch –d dev`** 删除dev分支
>　　**`git branch name`**  创建分支
>　　**`git stash`** 把当前的工作隐藏起来 等以后恢复现场后继续工作
>　　**`git stash list`** 查看所有被隐藏的文件列表
>　　**`git stash apply`** 恢复被隐藏的文件，但是内容不删除
>　　**`git stash drop`** 删除文件
>　　**`git stash pop`** 恢复文件的同时 也删除文件
>　　**`git remote `**查看远程库的信息
>　　**`git remote –v`** 查看远程库的详细信息
>　　**`git push origin master `** Git会把master分支推送到远程库对应的远程分支上

## 1.创建版本库
> \$  **`mkdir learngit`**
> \$ **`cd learngit`** (转到该位置)
> \$ **`pwd`**     (pwd命令用于显示当前目录)
> /Users/michael/learngit
>    通过命令 **`git init`** 把这个目录变成git可以管理的仓库
##2.基本操作
>    \$ **` git add readme.txt`**
>    \$ **`git  commit -m "wrote a readme file" `**
> [master (root-commit) eaadf4e] wrote a readme file
>1 file changed, 2 insertions(+) >create mode 100644 readme.txt
>
>简单解释一下git commit命令，-m后面输入的是本次提交的说明，可以输入任意内容，当然最好是有意义的，这样你就能从历史记录里方便地找到改动记录。
> 要随时掌握工作区的状态，使用git status命令:
> \$ **` git status`**
>如果git status告诉你有文件被修改过，用git diff可以查看修改内容:
>\$ **`git diff readme.txt`**
> git log命令显示从最近到最远的提交日志
> \$  **` git log `**
> 如果嫌输出信息太多，看得眼花缭乱的，可以试试加上**`--pretty=oneline`**参数
> 
> \$ **`git reset --hard HEAD^`**
> 首先，Git必须知道当前版本是哪个版本，在Git中，用HEAD表示当前版本，也就是最新的提交1094adb...（注意我的提交ID和你的肯定不一样），上一个版本就是HEAD^，上上一个版本就是HEAD^^，当然往上100个版本写100个^比较容易数不过来，所以写成HEAD~100。
> 
> \$**`git reset --hard commit_id`**
> HEAD指向的版本就是当前版本，因此，Git允许我们在版本的历史之间穿梭
> 
>\$ **`cat readme.txt`**
>用来查看内容
>
> \$ **`git reflog`**
> Git提供了一个命令git reflog用来记录你的每一次命令
> 
>   穿梭前，用git log可以查看提交历史，以便确定要回退到哪个版本。
>   要重返未来，用git reflog查看命令历史，以便确定要回到未来的哪个版本。
>    
>    工作区有一个隐藏目录.git，这个不算工作区，而是Git的版本库。
>   Git的版本库里存了很多东西，其中最重要的就是称为stage（或者叫index）的暂存区，还有Git为我们自动创建的第一个分支master，以及指向master的一个指针叫HEAD。
>    
>    场景1：当你改乱了工作区某个文件的内容，想直接丢弃工作区的修改时，用命令**`git checkout - - file`**。
>场景2：当你不但改乱了工作区某个文件的内容，还添加到了暂存区时，想丢弃修改，分两步，第一步用命令**`git reset HEAD <file>`**，就回到了场景1，第二步按场景1操作。
>    ( *git reset命令既可以回退版本，也可以把暂存区的修改回退到工作区。当我们用HEAD时，表示最新的版本。* )
>场景3：已经提交了不合适的修改到版本库时，想要撤销本次提交，参考版本回退一节，不过前提是没有推送到远程库。

### 删除文件
>一是确实要从版本库中删除该文件，那就用命令git rm删掉，并且git commit：
>\$ **`git rm test.txt`**  
>\$ **`git commit -m "remove test.txt"`**
>另一种情况是删错了，因为版本库里还有呢，所以可以很轻松地把误删的文件恢复到最新版本：
>\$ **`git checkout -- test.txt`**
>git checkout其实是用版本库里的版本替换工作区的版本，无论工作区是修改还是删除，都可以“一键还原”。
>
## 3.添加远程库(origin)
>要关联一个远程库，使用命令
>**` git  remote add origin git@ https//github.com:username/repo-name.git`**；
>关联后，使用命令
>**`git push -u origin master`**
>第一次推送master分支的所有内容；
>此后，每次本地提交后，只要有必要，就可以使用命令git push origin master推送最新修改；
>由于远程库是空的，我们第一次推送master分支时，加上了**-u**参数，Git不但会把本地的master分支内容推送到远程新的master分支，还会把本地的master分支和远程的master分支关联起来，在以后的推送或者拉取时就可以简化命令。
>
>分布式版本系统的最大好处之一是在本地工作完全不需要考虑远程库的存在，也就是有没有联网都可以正常工作，而SVN在没有联网的时候是拒绝干活的！当有网络的时候，再把本地提交推送一下就完成了同步，真是太方便了！
## 4.从远程库中克隆
> 首先，登陆GitHub，创建一个新的仓库
> 下一步是用命令git clone克隆一个本地库:
> \$ **` git clone git@github.com:gitName/reponstory.git`**
## 5.分支管理
>查看分支：**`git branch`**
>创建分支：**`git branch <name>`**
>切换分支：**`git checkout <name>`**
>创建+切换分支：**`git checkout -b <name>`**
>合并某分支到当前分支：**`git merge <name>`**
>删除分支：**`git branch -d <name>`**
##6.解决冲突
>当Git无法自动合并分支时，就必须首先解决冲突。解决冲突后，再提交，合并完成。
>解决冲突就是把Git合并失败的文件手动编辑为我们希望的内容，再提交。
>用**`git log --graph`**命令可以看到分支合并图。

##7.分支管理策略
>准备合并dev分支，请注意--no-ff参数，表示禁用Fast forward：
>\$**` git merge --no-ff -m "merge with no-ff" dev`**
>因为本次合并要创建一个新的commit，所以加上-m参数，把commit描述写进去。
>合并分支时，加上--no-ff参数就可以用普通模式合并，合并后的历史有分支，能看出来曾经做过合并，而fast forward合并就看不出来曾经做过合并。
## 8.Bug分支
>Git还提供了一个stash功能，可以把当前工作现场“储藏”起来，等以后恢复现场后继续工作：
>\$ **`git stash`**
>现在，用git status查看工作区，就是干净的（除非有没有被Git管理的文件），因此可以放心地创建分支来修复bug。
>存储工作现场：**`git stash`**
>查看工作现场：**`git stash list`**
>从工作现场恢复出来:**`git stash apply`**
>删除stash内容：**` git stash drop`**
>从工作现场恢复出来的同时把stash内容也删了：**`git stash pop`**
>你可以多次stash，恢复的时候，先用git stash list查看，然后恢复指定的stash，用命令：\$ **`git stash apply stash@{0}`**
## 9.Feature 分支
>开发一个新feature，最好新建一个分支；
如果要丢弃一个*`没有被合并过`*的分支，可以通过**`git branch -D <name>`**强行删除。
## 10.多人协作
>* master分支是主分支，因此要时刻与远程同步；
>* dev分支是开发分支，团队所有成员都需要在上面工作，所以也需要与远程同步；
>* bug分支只用于在本地修复bug，就没必要推到远程了，除非老板要看看你每周到底修复了几个bug；
>* feature分支是否推到远程，取决于你是否和你的小伙伴合作在上面开发。
>
> 当你的小伙伴从远程库clone时，默认情况下，你的小伙伴只能看到本地的master分支
> 现在，你的小伙伴要在dev分支上开发，就必须创建远程origin的dev分支到本地，于是他用这个命令创建本地dev分支：
> \$ **`git checkout -b dev origin/dev`**
> 也可以时不时地把dev分支push到远程：
> \$ **`git push origin dev`**
>
>  首先，可以试图用**`git push origin <branch-name>`**推送自己的修改；
如果推送失败，则因为远程分支比你的本地更新，需要先用**`git pull`**试图合并；
如果合并有冲突，则解决冲突，并在本地提交；
没有冲突或者解决掉冲突后，再用**`git push origin <branch-name>`**推送就能成功！
如果**`git pull`**提示no tracking information，则说明本地分支和远程分支的链接关系没有创建，用命令**`git branch --set-upstream-to <branch-name> origin/<branch-name>`**
>查看远程库信息，使用**`git remote -v`**；
本地新建的分支如果不推送到远程，对其他人就是不可见的；
从本地推送分支，使用**`git push origin branch-name`**，如果推送失败，先用**`git pull`**抓取远程的新提交；
在本地创建和远程分支对应的分支，使用**`git checkout -b branch-name origin/branch-name`**，本地和远程分支的名称最好一致；
建立本地分支和远程分支的关联，使用**`git branch --set-upstream branch-name origin/branch-name`**；
从远程抓取分支，使用**`git pull`**，如果有冲突，要先处理冲突。

## 11.创建标签
>命令**`git tag <tagname> <commit id>`**用于新建一个标签，默认为HEAD，也可以指定一个commit id；
>命令**`git tag -a <tagname> -m "blablabla..." <commit id>`**"可以指定标签信息；
>命令**`git tag`**可以查看所有标签。
## 12.操作标签
>* 命令**`git push origin <tagname>`**可以推送一个本地标签；
>* 命令**`git push origin --tags `**可以推送全部未推送过的本地标签；
>* 命令**`git tag -d <tagname>`**可以删除一个本地标签；
>* 命令**`git push origin :refs/tags/<tagname>`**可以删除一个远程标签。
## 13.忽略特殊文件
>1. 忽略某些文件时，需要编写.gitignore；
>2.  .gitignore文件本身要放到版本库里，并且可以对.gitignore做版本管理！
>3. * 使用Windows的童鞋注意了，如果你在资源管理器里新建一个.gitignore文件，它会非常弱智地提示你必须输入文件名，但是在文本编辑器里“保存”或者“另存为”就可以把文件保存为.gitignore了。
* 有些时候，你想添加一个文件到Git，但发现添加不了，原因是这个文件被.gitignore忽略了：如果你确实想添加该文件，可以用-f强制添加到Git：\$**` git add -f App.class`**
* 或者你发现，可能是.gitignore写得有问题，需要找出来到底哪个规则写错了，可以用git check-ignore命令检查：\$ **`git check-ignore -v App.class`**

## 14.配置别名
>eg: 用st来代替status \$ **`git config --global alias.st status`**



## 15.git pull –rebase 理解

* git pull –rebase这个命令做了以下内容：  
  * a.把你 commit 到本地仓库的内容，取出来放到暂存区(stash)（这时你的工作区是干净的）  
  * b.然后从远端拉取代码到本地，由于工作区是干净的，所以不会有冲突  
  * c.从暂存区把你之前提交的内容取出来，跟拉下来的代码合并  所以 rebase 在拉代码前要确保你本地工作区是干净的，如果你本地修改的内容没完全 commit 或者 stash，就会 rebase 失败。 

