一。设置用户名和邮箱

二，创建文本库

1. D盘 –> www下 目录下新建一个testgit版本库 

   2.pwd 命令是用于显示当前的目录。 

  3.通过命令 git init 把这个目录变成git可以管理的仓库

 4.将文件添加到版权库：第一步，使用命令 git add readme.txt添加到暂存区里面去，没有任何提示，            说明已经添加成功了 ；第二步，用命令 git commit告诉Git，把文件提交到仓库，可以通过命令git status来查看是否还有文件未提交 ；  

5.查看修改的内容：git diff readme.txt ;

6.查看修改的日志：git log ；

7.版本回退操作：git reset  --hard HEAD^ ；git reset  --hard HEAD^ ^；git reset  --hard HEAD~100  ，再来查看下 readme.txt内容如下：通过命令cat readme.txt查看 ；

8,获取版本号：git reflog；恢复该版本号：git reset  --hard 6fcfc89 ；

三，工作区和暂存区的区别：

​       工作区：就是你在电脑上看到的目录，比如目录下testgit里的文件(.git隐藏目       录版本库除外)。或者以后需要再新建的目录文件等等都属于工作区范畴。

​       版本库(Repository)：工作区有一个隐藏目录.git,这个不属于工作区，这是版本库。其中版本库里面存了很多东西，其中最重要的就是stage(暂存区)，还有Git为我们自动创建了第一个分支master,以及指向master的一个指针HEAD。使用git  add提交文件实际上就是吧文件提交到暂存区；git commit提交实际上就是吧暂存区的所有内容提交到当前分支上；

四：Git撤销修改和删除文件操作：

​     1.撤销修改（多种方法）：

​           1）如果我知道要删掉那些内容的话，直接手动更改去掉那些需要的文件，    然后add添加到暂存区，最后commit掉。（ ？） 

​           2）我可以按以前的方法直接恢复到上一个版本。使用 git reset  --hard HEAD^ 。

​	 3）git checkout  --  readme.txt ：有两种情况，第一，readme.txt自动修改后，还没有放到暂存区，使用 撤销修改就回到和版本库一模一样的状态；另外一种是readme.txt已经放入暂存区了，接着又作了修改，撤销修改就回到添加暂存区后的状态（换句话来说就是只能修改未放到暂存区的）；注意：命令git checkout -- readme.txt 中的 -- 很重要，如果没有 -- 的话，那么命令变成创建分支了 ;

​     2.删除文件：   一般情况下，可以直接在文件目录中把文件删了，或者使用如上rm命令：rm b.txt ，如果我想彻底从版本库中删掉了此文件的话，可以再执行commit命令 提交掉；只要没有commit之前，如果想在版本库中恢复此文件： git checkout  -- b.txt   ；

五，远程仓库：

​       1.注册github账号，创建ssh key，登录github,打开” settings”中的SSH Keys页面，然后点击“Add SSH Key”,填上任意title，在Key文本框里黏贴id_rsa.pub文件的内容。 另：id_rsa是私钥，不能泄露出去，id_rsa.pub是公钥，可以放心地告诉任何人。 

​       2.创建远程库，接着，使用命令行git remote add origin https://github.com/tugenhua0707/testgit.git ，然后使用命令行git push -u origin master把当前分支master推送到远程 ,从现在起，只要本地作了提交，就可以通过如下命令git push origin master把本地master分支的最新修改推送到github上了，现在你就拥有了真正的分布式版本库了。

   3,如何远程库克隆：使用命令行git clone https:github.com/LShangRong/testgit2即可成功复制。

六，创建与合并分支。

​       1，git checkout  –b div 表示创建并切换 ,git branch dev,

　　git checkout dev表示切换分支；

​        2，用git merge dev 合并dev上的内容到master注分支上，合并完成就可以删除Dev分支了

​        查看分支：git branch

　　创建分支：git branch name

　　切换分支：git checkout name

　　创建+切换分支：git checkout –b name

　　合并某分支到当前分支：git merge name

　　删除分支：git branch –d name

​       3，解决合并冲突：将分支的内容修改和住址上的一模一样即可

​       4.分支管理策略：通常合并分支时，git一般使用”Fast forward”模式，在这种       模式下，删除分支后，会丢掉分支信息，现在我们来使用带参数 –no-ff来禁         用”Fast forward”模式：

1. ）创建一个dev分支。

2. ）修改readme.txt内容。

3. ）添加到暂存区。

4. ）切换回主分支(master)。

5. ）合并dev分支，使用命令 git merge –no-ff  -m “注释” dev

6. ）查看历史记录

   另：首先master主分支应该是非常稳定的，也就是用来发布新版本，一般情况下不允许在上面干活，干活一般情况下在新建的dev分支上干活，干完后，比如上要发布，或者说dev分支代码稳定后可以合并到主分支master上来。 

七，bug分支：开发中，会经常碰到bug问题，那么有了bug就需要修复，在Git中，分支是很强大的，每个bug都可以通过一个临时分支来修复，修复完成后，合并分支，然后将临时的分支删除掉。Git提供了一个stash功能，可以把当前工作现场 ”隐藏起来”，等以后恢复现场后继续工作 ，我可以通过创建issue-404分支来修复bug ，修复完成后，切换到master分支上，并完成合并，最后删除issue-404分支 ，删完之后可以用git status来查看工作区是否是干净的，接着，找回原来bug前的工作区， git stash list来查看 工作现场还在，Git把stash内容存在某个地方了，但是需要恢复一下，可以使用如下2个方法：第一，git stash apply恢复，恢复后，stash内容并不删除，你需要使用命令git stash drop来删除；另一种方式是使用git stash pop,恢复的同时把stash内容也删除了。

八，多人协作：

​           1，当你从远程库克隆时候，实际上Git自动把本地的master分支和远程的master分支对应起来了，并且远程库的默认名称是origin。要查看远程库的信息 使用 git remote；要查看远程库的详细信息 使用 git remote –v（含https:/.....）。

​          2，推送分支：推送分支就是把该分支上所有本地提交到远程库中，推送时，要指定本地分支，这样，Git就会把该分支推送到远程库对应的远程分支上： 使用命令 git push origin master。若要推送到Dev分支上，则git push origin dev ；那么一般情况下，那些分支要推送呢：一，master分支是主分支，因此要时刻与远程同步。二，一些修复bug分支不需要推送到远程去，可以先合并到主分支上，然后把主分支master推送到远程去。

​	3，抓取分支：多人协作时，大家都会往master分支上推送各自的修改。现在我们可以模拟另外一个同事，可以在另一台电脑上（注意要把SSH key添加到github上）或者同一台电脑上另外一个目录克隆，新建一个目录名字叫testgit2 ；现在我们要在dev分支上做开发，就必须把远程的origin的dev分支到本地来，于是可以使用命令创建本地dev分支：git checkout  –b dev origin/dev ，完成后再推送到远程库中去。注意：当两个人同时修改同个文件，同个地方则会推送失败，此时用git pull把最新的提交从origin/dev抓下来，然后在本地合并，解决冲突，再推送 ；若git pull失败，原因是没有指定本地dev分支与远程origin/dev分支的链接 （git branch --set-upstream dev origin/dev）;若git pull成功，但是合并有冲突，需要手动解决，解决的方法和分支管理中的 解决冲突完全一样。解决后，提交，再push;

​         因此：多人协作工作模式一般是这样的：首先，可以试图用git push origin branch-name推送自己的修改；其次，如果推送失败，则因为远程分支比你的本地更新早，需要先用git pull试图合并；最后，如果合并有冲突，则需要解决冲突，并在本地提交。再用git push origin branch-name推送。

 　以上Git基本常用命令如下：

　　mkdir：         XX (创建一个空目录 XX指目录名)

　　pwd：          显示当前目录的路径。

　　git init          把当前的目录变成可以管理的git仓库，生成隐藏.git文件。

　　git add XX       把xx文件添加到暂存区去。

　　git commit –m “XX”  提交文件 –m 后面的是注释。

　　git status        查看仓库状态

　　git diff  XX      查看XX文件修改了那些内容

　　git log          查看历史记录

　　git reset  --hard HEAD^ 或者 git reset  --hard HEAD~ 回退到上一个版本

　　(如果想回退到100个版本，使用git reset –hard HEAD~100 )

　　cat XX         查看XX文件内容

　　git reflog       查看历史记录的版本号id

　　git checkout -- XX  把XX文件在工作区的修改全部撤销。

　　git rm XX          删除XX文件

　　git remote add origin <https://github.com/tugenhua0707/testgit> 关联一个远程库

　　git push –u(第一次要用-u 以后不需要) origin master 把当前master分支推送到远程库

　　git clone <https://github.com/tugenhua0707/testgit>  从远程库中克隆

　　git checkout –b dev  创建dev分支 并切换到dev分支上

　　git branch  查看当前所有的分支

　　git checkout master 切换回master分支

　　git merge dev    在当前的分支上合并dev分支

　　git branch –d dev 删除dev分支

　　git branch name  创建分支

　　git stash 把当前的工作隐藏起来 等以后恢复现场后继续工作

　　git stash list 查看所有被隐藏的文件列表

　　git stash apply 恢复被隐藏的文件，但是内容不删除

　　git stash drop 删除文件

　　git stash pop 恢复文件的同时 也删除文件

　　git remote 查看远程库的信息

　　git remote –v 查看远程库的详细信息

　　git push origin master  Git会把master分支推送到远程库对应的远程分支上

 九，标签管理：

​         1.Git的标签虽然是版本库的快照，但其实它就是指向某个commit的指针（跟分支很像对不对？但是分支可以移动，标签不能移动），所以，创建和删除标签都是瞬间完成的。还有tag就是一个让人容易记住的有意义的名字，它跟某个commit绑在一起。 

​	2.创建标签：首先，切换到需要打标签的分支上 ，然后，敲命令git tag v1.0就可以打一个新标签  ；若忘了打标签，方法是找到历史提交的commit id，然后打上就可以了  ，命令是：git log --pretty=oneline --abbrev-commit；注意，标签不是按时间顺序列出，而是按字母排序的。可以用git show v0.9查看标签信息 ；创建带有说明的标签，用`-a`指定标签名，`-m`指定说明文字 ；注意：标签总是和某个commit挂钩。如果这个commit既出现在master分支，又出现在dev分支，那么在这两个分支上都可以看到这个标签。 

​      3，操作标签：删除（ git tag -d v0.1，因为创建的标签都只存储在本地，不会自动推送到远程。所以，打错的标签可以在本地安全删除。 ），推送标签到远程（git push origin v1.0，推送所有未推送的标签git push origin --tags），如果标签已经推送到远程，要删除远程标签就麻烦一点，先从本地删除，再从远程删除 git push origin :refs/tags/v0.9，切要看看是否真的从远程库删除了标签，可以登陆GitHub查看。 

十，使用GitHub；

十一，使用码云：

​        注意点：1，使用命令`git remote add`时报错 ，这说明本地库已经关联了一个名叫`origin`的远程库，此时，可以先用`git remote -v`查看远程库信息，接着可以选择删除已有的GitHub远程库，再关联码云的远程库（填写正确的路径名：git remote add origin git@gitee.com:liaoxuefeng/learngit.git）；2，如果要同时关联多个远程库，则git给远程库起的默认名称是`origin`，如果有多个远程库，我们需要用不同的名称来标识不同的远程库，仍然以`learngit`本地库为例，我们先删除已关联的名为`origin`的远程库：git remote rm origin，然后，先关联GitHub的远程库，接着，再关联码云的远程库；如果要推送到GitHub，使用命令： git push github master；如果要推送到码云，使用命令：git push gitee master。

十二，自定义Git

​        1，忽略特殊文件：Git工作区的根目录下创建一个特殊的`.gitignore`文件，然后把要忽略的文件名填进去，Git就会自动忽略这些文件。 忽略文件的原则是：忽略操作系统自动生成的文件，比如缩略图等；忽略编译生成的中间文件、可执行文件等，也就是如果一个文件是通过另一个文件自动生成的，那自动生成的文件就没必要放进版本库，比如Java编译产生的`.class`文件；忽略你自己的带有敏感信息的配置文件，比如存放口令的配置文件。使用Windows的童鞋注意了，如果你在资源管理器里新建一个`.gitignore`文件，它会非常弱智地提示你必须输入文件名，但是在文本编辑器里“保存”或者“另存为”就可以把文件保存为`.gitignore了。有些时候，你想添加一个文件到Git，但发现添加不了，原因是这个文件被.gitignore`忽略了；如果你确实想添加该文件，可以用`-f`强制添加到Git： git add -f App.class；或者你发现，可能是`.gitignore`写得有问题，需要找出来到底哪个规则写错了，可以用`git check-ignore`命令检查： git check-ignore -v App.class；

​        2，配置别名：需要敲一行命令，告诉Git，以后`st`就表示`status` ：git config --global alias.st status，`--global`参数是全局参数，也就是这些命令在这台电脑的所有Git仓库下都有用，如果不加，那只针对当前的仓库起作用；还可以为两个单词设置别名；每个仓库的Git配置文件都放在`.git/config`文件中 ，别名就在`[alias]`后面，要删除别名，直接把对应的行删掉即可；

 十三，搭建Git服务器。

 

 