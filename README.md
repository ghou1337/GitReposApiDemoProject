# GitHub Api Repositories Parsing Program

This program aims to locate a user, retrieve all their Git repositories,
and obtain the latest commit for each branch that is not a fork,
along with the user's login and repository name.

# Installation

```
git clone ----
```

# Plugins

-spring-boot-starter-web
-lombok
-spring-webflux
-spring-boot-starter-test

# Configuration

All configuration data placed in application.properties file

```
gitApi.barerToken = Bearer UseYourBarerGitToken     # User your personal bearer (jwt) token with "Bearer" word before it
gitApi.acceptHeader = application/vnd.github+json   # Accept request header
gitApi.gitApiVersion = 2022-11-28                   # Git Api version header
```

# Commands

'''
localhost:8080/api/user/repos/get-all
'''

# Author

-ghou1337

