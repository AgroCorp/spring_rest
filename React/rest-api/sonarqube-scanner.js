const scanner = require("sonarqube-scanner");
scanner(
  {
    serverUrl: "http://192.168.2.55:9002",
    options: {
      "sonar.sources": "./React/rest-api",
      "sonar.token": "sqa_56e90bb26a71c833ced6d7eea8b424be1c87e49e",
      "sonar.projectKey": "springReact",
      "sonar.projectName": "springReact",
      "sonar.exclusions": "node_modules/**",
    },
  },
  () => process.exit()
);
