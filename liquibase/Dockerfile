FROM liquibase/liquibase
ADD /Users/shaplovdv/IdeaProjects/kuber_users_crud/liquibase/changelog /liquibase/changelog
CMD ["sh", "-c", "docker-entrypoint.sh --url=${URL} --username=${USERNAME} --password=${PASSWORD} --classpath=/liquibase/changelog --changeLogFile=relative/changelog.xml update"]