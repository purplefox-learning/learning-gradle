Before Run: Delete remote repo on bitbucket; delete the local directory LocalGitRepoRoot; 

TO RUN: gradle -Djavax.net.ssl.trustStore=default_plus_scbbitbucket_cacerts -Pservice=scbCoreBankingCustomer

TO VERIFY: git ls-tree -r --name-only master
