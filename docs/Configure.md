## Apollo Configuration


Apollo includes some basic configuration parameters that are specified in configuration files. The most important
parameters are the database parameters in order to get Apollo up and running. Other options besides the database
parameters can be configured via the config files, but note that many parameters can also be configured via the web
interface.

Note: Configuration options may change over time, as more configuration items are integrated into the web interface.


### Main configuration

The main configuration settings for Apollo are stored in `grails-app/conf/Config.groovy`, but you can override settings
in your `apollo-config.groovy` file (i.e. the same file that contains your database parameters). Here are the defaults
that are defined in the Config.groovy file:

``` 
// default apollo settings
apollo {
  default_minimum_intron_size = 1
  history_size = 0
  overlapper_class = "org.bbop.apollo.sequence.OrfOverlapper"
  track_name_comparator = "/config/track_name_comparator.js"
  use_cds_for_new_transcripts = true
  user_pure_memory_store = true
  translation_table = "/config/translation_tables/ncbi_1_translation_table.txt"
  is_partial_translation_allowed = false // unused so far
  get_translation_code = 1
  sequence_search_tools = [
    blat_nuc: [
      search_exe: "/usr/local/bin/blat",
      search_class: "org.bbop.apollo.sequence.search.blat.BlatCommandLineNucleotideToNucleotide",
      name: "Blat nucleotide",
      params: ""
    ],
    blat_prot: [
      search_exe: "/usr/local/bin/blat",
      search_class: "org.bbop.apollo.sequence.search.blat.BlatCommandLineProteinToNucleotide",
      name: "Blat protein",
      params: ""
      tmp_dir: "/opt/apollo/tmp" //optional param, uses system tmp dir by default
    ]
  ]    
      


  splice_donor_sites = [ "GT" ]
  splice_acceptor_sites = [ "AG"]
  gff3.source= "." bootstrap = false

  info_editor = {
    feature_types = "default"
    attributes = true
    dbxrefs = true
    pubmed_ids = true
    go_ids = true
    comments = true
  }
}
```

These settings are essentially the same familiar parameters from a config.xml file from previous Apollo versions.  The
defaults are generally sufficient, but as noted above, you can override any particular parameter in your
`apollo-config.groovy` file, e.g. you can add override configuration any given parameter as follows:

``` 
grails {
  apollo.get_translation_code = 1
  apollo {
    use_cds_for_new_transcripts = true
    default_minimum_intron_size = 1
    get_translation_code = 1  // identical to the dot notation
  }
}
```

### Logging configuration

To over-ride the default logging, you can look at the logging configurations from
[Config.groovy](https://github.com/GMOD/Apollo/blob/master/grails-app/conf/Config.groovy) and override or modify them in
`apollo-config.groovy`.

``` 
log4j.main = {
    error 'org.codehaus.groovy.grails.web.servlet',  // controllers
          'org.codehaus.groovy.grails.web.pages',    // GSP
          'org.codehaus.groovy.grails.web.sitemesh', // layouts
           ...
    warn 'grails.app'
}
```

Additional links for log4j:

- Advanced log4j configuration:
  http://blog.andresteingress.com/2012/03/22/grails-adding-more-than-one-log4j-configurations/
- Grails log4j guide: http://grails.github.io/grails-doc/2.4.x/guide/single.html#logging


### Canned comments


Canned comments are configured via the admin panel on the web interface, so they are not currently configured via the
config files.

View your instances page for more details e.g. http://localhost:8080/apollo/cannedComment/


### Search tools

Apollo can be configured to work with various sequence search tools. UCSC's BLAT tool is configured by default and you
can customize it as follows:

``` 
sequence_search_tools = [
  blat_nuc: [
    search_exe: "/usr/local/bin/blat",
    search_class: "org.bbop.apollo.sequence.search.blat.BlatCommandLineNucleotideToNucleotide",
    name: "Blat nucleotide",
    params: ""
  ],
  blat_prot: [
    search_exe: "/usr/local/bin/blat",
    search_class: "org.bbop.apollo.sequence.search.blat.BlatCommandLineProteinToNucleotide",
    name: "Blat protein",
    params: "",
    tmp_dir: "/opt/apollo/tmp" //optional, uses system tmp dir by default
  ]
  your_custom_search_tool: [
    search_exe: "/usr/local/customtool",
    search_class: "org.your.custom.Class",
    name: "Custom search"
  ]
]

```

When you setup your organism in the web interface, you can then enter the location of the sequence search database for
BLAT.


Note: If the BLAT binaries reside elsewhere on your system, edit the search_exe location in the config to point to your
BLAT executable.

### Data adapters


Data adapters for Apollo provide the methods for exporting annotation data from the application. By default, GFF3
and FASTA adapters are supplied. They are configured to query your IOService URL e.g.
http://localhost:8080/apollo/IOService with the customizable query

``` 
data_adapters = [[
  permission: 1,
  key: "GFF3",
  data_adapters: [[
    permission: 1,
    key: "Only GFF3",
    options: "output=file&format=gzip&type=GFF3&exportGff3Fasta=false"
  ],
  [
    permission: 1,
    key: "GFF3 with FASTA",
    options: "output=file&format=gzip&type=GFF3&exportGff3Fasta=true"
  ]]
],
[
  permission: 1,
  key : "FASTA",
  data_adapters :[[
    permission : 1,
    key : "peptide",
    options : "output=file&format=gzip&type=FASTA&seqType=peptide"
  ],
  [
    permission : 1,
    key : "cDNA",
    options : "output=file&format=gzip&type=FASTA&seqType=cdna"
  ],
  [
    permission : 1,
    key : "CDS",
    options : "output=file&format=gzip&type=FASTA&seqType=cds"
  ]]
]]
```

#### Default data adapter options

The options available for the data adapters are configured as follows

- type: `GFF3` or `FASTA`
- output: can be `file` or `text`. `file` exports to a file and provides a UUID link for downloads, text just outputs to
  stream.
- format: can by `gzip` or `plain`. `gzip` offers gzip compression of the exports, which is the default.
- exportSequence: `true` or `false`, which is used to include FASTA sequence at the bottom of a GFF3 export


### Supported annotation types

Many configurations will require you to define which annotation types the configuration will apply to. Apollo supports
the following "higher level" types (from the Sequence Ontology):

* sequence:gene
* sequence:pseudogene
* sequence:transcript
* sequence:mRNA
* sequence:tRNA
* sequence:snRNA
* sequence:snoRNA
* sequence:ncRNA
* sequence:rRNA
* sequence:miRNA
* sequence:repeat_region
* sequence:transposable_element


### Apache / Nginx configuration

Oftentimes, admins will put use Apache or Nginx as a reverse proxy so that the requests to a main server can be
forwarded to the tomcat server.  This setup is not necessary, but it is a very standard configuration.  

Note that we use the SockJS library, which will downgrade to long-polling if websockets are not available, but since
websockets are preferable, it helps to take some extra steps to ensure that the websocket calls are proxied or forwarded
in some way too.
If you are using tomcat 7, please make sure to use the most recent stable version, which supports web sockets by default.  Using older versions (e.g. 7.0.26) websockets may not be included by default and you will need to include an additional .jar file.

#### Apache Proxy 

The most simple setup on apache is as follows.. Here is the most basic configuration for a reverse proxy:


``` 
ProxyPass  /apollo http://localhost:8080/apollo
ProxyPassReverse  /apollo http://localhost:8080/apollo
```

Note: that a reverse proxy _does not_ use `ProxyRequests On` (which turns on forward proxying, which is dangerous)


Also note: This setup will use downgrade to use AJAX long-polling without the websocket proxy being configured.


To setup the proxy for websockets, you can use mod_proxy_wstunnel, first load the module

``` 
LoadModule proxy_wstunnel_module libexec/apache2/mod_proxy_wstunnel.so
```

Then add extra ProxyPass calls for the websocket "endpoint" called `/apollo/stomp`

``` 
ProxyPass /apollo/stomp  ws://localhost:8080/apollo/stomp
ProxyPassReverse /apollo/stomp ws://localhost:8080/apollo/stomp
```

##### Debugging proxy issues

Note: if your webapp is accessible but it doesn't seem like you can login, you may need to customize the
ProxyPassReverseCookiePath

For example, if you proxied to a different path, you might have something like this

``` 
ProxyPass  /testing http://localhost:8080
ProxyPassReverse  /testing http://localhost:8080
ProxyPassReverseCookiePath / /testing
```

Then your application might be accessible from http://localhost/testing/apollo


#### Nginx Proxy (from version 1.4 on)

Your setup may vary, but setting the upgrade headers can be used for the websocket configuration
http://nginx.org/en/docs/http/websocket.html

``` 
    map $http_upgrade $connection_upgrade {
        default upgrade;
        ''      close;
    }
    
    
    server {
        # Main
        listen   80; server_name  myserver;
        
        # http://nginx.org/en/docs/http/websocket.html
        location /ApolloSever {
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection $connection_upgrade;
            proxy_pass      http://127.0.0.1:8080;
        }
    }
```


### Upgrading existing instances

There are several scripts for migrating from older instances. See the [migration guide](Migration.md) for details.
Particular notes:

Note: Apollo does not require using the `add-webapollo-plugin.pl` because the plugin is loaded implicitly by
including the client/apollo/json/annot.json file at run time.

#### Upgrading existing JBrowse data stores

It is not necessary to change your existing JBrowse data directories to use Apollo 2.x, you can just point to existing
data directories from your previous instances.

#### Adding custom CSS for track styling for JBrowse

There are a variety of different ways to include new CSS into the browser, but the easiest might be the following


Add the following statement to your trackList.json:

``` 
    "css" : "data/yourfile.css"
```


Then just place your CSS file in your organism's data directory.

##### Adding custom CSS globally for JBrowse

If you want to add CSS that is used globally for JBrowse, you can edit the CSS in the client/apollo/css folder, but
since you need to re-deploy the app every time for updates, it is easier to just edit the data directories for your
organisms (you do not need to re-deploy the app when you are editing organism specific data, since this is outside of
the webapp directory and is not deployed with the WAR file)


#### Adding custom CSS globally for the GWT app


If you want to style the GWT sidebar, generally the bootstrap theme is used but extra CSS is also included from
web-app/annotator/theme.css which overrides the bootstrap theme


#### Adding / using proxies

If you are https, or choose to use separate services rather than the default provided, you can setup a pass-through proxy or modify a particular URL. 

This service is only available to logged-in users. 

The internal proxy URL is: 

```<apollo url>/proxy/request/<encoded_proxy_url>/```

For example if your URL the URL we want to proxy:

```http://golr.geneontology.org/solr/select```

encoded:

```http%3A%2F%2Fgolr.geneontology.org%2Fsolr%2Fselect```

If you user is logged-in and you pass in:

```http://localhost/apollo/proxy/request/http%3A%2F%2Fgolr.geneontology.org%2Fsolr%2Fselect?testkey=asdf&anotherkey=zxcv```

This will get proxied to:

```http://golr.geneontology.org/solr/select?testkey=asdf&anotherkey=zxcv```

If you choose to use another proxy service, you can go to the "Proxy" page (as administrator). 
Internally used proxies are provided by default. 
The order the final URL is chosen in is 'active' and then 'fallbackOrder'.  



