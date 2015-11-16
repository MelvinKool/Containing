============
Contributing
============

This project is a school project. All contributions will be made by members of
the assigned team. This manual hopes to document the steps of contribution.

Git
---

This is a git project. Because not all team members may be aware of git
procedures and/or git etiquette, the procedures will be documented here in a
codewise manner.

The recommended way of interacting with git is through the command line
interface. It allows the most control over all interactivity with git, because
you have to specify to the system exactly what you want it to do. While GUI
clients are technically fine, they hide a lot of things that are valuable.

This guide will cover git interaction throught the command line interface. The
steps may be translated to steps in a graphical user interface.

Glossary
~~~~~~~~

Some oft-used git terms:

- Repository: Easiest to imagine as a project's folder. A repository contains
  all of the project files (including documentation), and stores each file's
  revision history. A repository can be remote (on GitHub) or local (on your
  machine). Most work is done locally.
- Commit: The basis of git functionality. A commit is like "saving" changes
  to your repository. A commit is a ``diff`` between two revisions of the
  repository.
- Branch: A parallel version of the repository. Where the ``master`` branch is
  the stable reference version of the project, other branches may be created so
  to work on more difficult features. This assures that the ``master`` branch
  is never unstable. Branches can be merged back into ``master`` by the
  maintainer.
- Merge: Merging one branch into another, unifying the commits into one branch.
  Merging can be done in a multitude of fashions.
- Upstream: The reference repository. Best explained by comparing to Linux.
  Linus Torvalds (the creator of Linux) is the upstream maintainer of the Linux
  kernel. Canonical (creator of Ubuntu) has their own version of Linux.
  Canonical's version of Linux is *downstream*, because everybody recognises
  Linus Torvalds' repository as "the one true Linux", not Canonical's.
- Origin: Your own remote repository.
- Maintainer: The "admin" of a repository who has read/write access.
- Pull: Fetching changes hosted on a remote repository and merging them into
  your local branch.
- Pull request: A proposal made to a maintainer to have *them* merge in a
  branch that was made in a downstream repository. Pull requests are typically
  targetted at the upstream ``master`` branch.

Set up your GitHub page
~~~~~~~~~~~~~~~~~~~~~~~

If you do not yet have a GitHub account, make one. This should require no
documentation.

On the `upstream repository <https://github.com/Rubykuby/containing>`_, click
"Fork" in the upper right corner. This will create a clone of the upstream
repository. Because this is a fork, you should *never ever push to the
master branch*. The master branch should stay in sync with the upstream's
master branch.

Setting up git locally
~~~~~~~~~~~~~~~~~~~~~~

Make sure that you have git installed. For a command line interface version,
see `here <http://git-scm.com/downloads>`_. You may want to allow git to be
added to the ``PATH`` if you're using the Windows version. The ``git-cheetah``
option in the Windows version is also nice.

Open up your terminal, and ``cd`` to the directory that holds all of your
code projects. This is typically called ``Workspace`` or ``Projects``.

Setting up involves the following steps::

    # Clone your fork.
    $ git clone https://github.com/YOURNAME/containing

    # Change into your reposoitory's directory.
    $ cd containing

    # Set up the upstream repository.
    $ git remote add upstream https://github.com/Rubykuby/containing

    # Fetch the entire upstream repository into a local cache (.git).
    $ git fetch upstream

    # Set your local master branch to track the upstream branch, rather than
    # your own remote master branch. This ensures that every ``git pull``
    # pulls in from upstream/master, and that you cannot ``git push`` to
    # upstream/master. This is beneficial, because you should NEVER EVER do
    # commits to the master branch.
    $ git branch -u upstream/master

Your local git repository is now ready for use.

Develop a new feature (basic workflow)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Because you're not allowed to **ever ever ever** work in the master branch, you
create a new branch where you do your work in.

For every feature you develop, you branch away from master into a branch name
that concisely describes what you're trying to achieve. You write away, do some
commits along the way, and once you've reached a good final state on your
feature, you push your changes to your own fork repository. From your GitHub
page, you then make a pull request into the master branch of the reference
repository, and your work is done.

After your work is done, you locally switch back to the master branch, pull in
any new changes from the upstream master branch (effectively synchronising your
local master branch with the upstream master branch), and create a new branch
for another new feature.

This looks a little like this::

    # Make sure you're on the master branch.
    $ git status
    On branch master
    Your branch is up-to-date with 'upstream/master'.

    # If you're not on the master branch, switch to it.
    $ git checkout master

    # Make sure your master branch is up to date.
    # NOTE: ``git status`` may lie to you about whether you're up to date. As
    # a rule, just pull anyway.
    $ git pull upstream master

    # Create a new branch. Substitute ``$BRANCH_NAME`` with whatever.
    $ git checkout -b $BRANCH_NAME

    # Leave the terminal and work on your code. Once you've reached a state
    # where you think it useful to make a commit, return to the terminal.

    # Check which files you've changed.
    $ git status
    On branch contributing
    Untracked files:
      (use "git add <file>..." to include in what will be committed)

           CONTRIBUTING.rst
           unrelated_file.txt

    nothing added to commit but untracked files present (use "git add" to track)

    # Stage the files that you wish to include in your commit. NEVER ADD
    # USER-SPECIFIC FILES OR BINARY FILES.
    $ git add $FILE_THAT_NEEDS_TO_BE_STAGED

    # Make sure that all files that had to be staged have actually been staged.
    $ git status
    On branch contributing
    Changes to be committed:
      (use "git reset HEAD <file>..." to unstage)

            new file:   CONTRIBUTING.rst

    Untracked files:
      (use "git add <file>..." to include in what will be committed)

           unrelated_file.txt

    # If you're certain that you're ready to make your commit, actually make
    # your commit. Include a good, descriptive message.
    $ git commit -m "This message describes my commit"

    # Work on more code and make more commits until you're finished.

    # Once finished, push your commits to your remote repository. Note that
    # "origin" is a pointer to your own remote repository.
    # Similary, "upstream" would be a pointer to the upstream remote
    # repository.
    $ git push origin $BRANCH_NAME

    # Make a pull request on GitHub if you think that your branch should be
    # merged into upstream.

    # Switch back to master.
    $ git checkout master

    # Make sure master is up to date.
    $ git pull upstream master

    # OPTIONAL: Synchronise your remote origin/master with upstream/master.
    $ git push origin master

This procedure is standard workflow for git.

Working on someone else's branch
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Sometimes, you may find it necessary to work on a branch that you do not fork
from upstream/master. This workflow is a little different. Pull requests should
be made to the author of the branch, rather than upstream.

This looks something like this::

    # Add that person's repository to your list of remotes.
    $ git remote add $REPO_NAME https://github.com/THEIR_NAME/containing

    # Fetch all their stuff into your local cache.
    $ git fetch $REPO_NAME

    # Fork their branch.
    git checkout -b $BRANCH_NAME $REPO_NAME/$BRANCH_NAME

    # Do your stuff. Basically standard git workflow here. Your pull requests
    # should be targetted at THEIR branch.

    # OPTIONAL: Remove their repository from your list of remotes.
    $ git remote remove $REPO_NAME

    # OPTIONAL: Remove the created branch.
    $ git branch -D $BRANCH_NAME
