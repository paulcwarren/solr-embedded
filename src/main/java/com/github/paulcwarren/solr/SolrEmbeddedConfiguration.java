package com.github.paulcwarren.solr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.PreDestroy;

import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.request.CoreAdminRequest;
import org.apache.solr.core.NodeConfig;
import org.apache.solr.core.SolrResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SolrEmbeddedConfiguration {

    private static final String CONFIGSET_DIR = "src/main/resources/configsets";

    public String coreName() {
    	return "exampleCollection";
    }
    
	@Bean
	public SolrClient solrClient() throws IOException, SolrServerException {
//        String targetLocation = SolrEmbeddedConfiguration.class
//                .getProtectionDomain().getCodeSource().getLocation().getFile() + "/..";
        String targetLocation = "/tmp";

        String solrHome = targetLocation + "/solr";
		
        final File solrHomeDir = new File(solrHome);
        if (solrHomeDir.exists()) {
            FileUtils.deleteDirectory(solrHomeDir);
            solrHomeDir.mkdirs();
        } else {
            solrHomeDir.mkdirs();
        }

        deployConfigsets(solrHome);

        final SolrResourceLoader loader = new SolrResourceLoader(solrHomeDir.toPath());
        final Path configSetPath = Paths.get(solrHome + "/configsets").toAbsolutePath();

        final NodeConfig config = new NodeConfig.NodeConfigBuilder("embeddedSolrServerNode", loader)
                .setConfigSetBaseDirectory(configSetPath.toString())
                .build();

        final EmbeddedSolrServer embeddedSolrServer = new EmbeddedSolrServer(config, coreName());

        final CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
        createRequest.setCoreName(coreName());
        createRequest.setConfigSet(coreName());
        embeddedSolrServer.request(createRequest);

        return embeddedSolrServer;
	}
	
	protected void deployConfigsets(String targetDir) {
	     byte[] buffer = new byte[1024];
	     
	     try {
	    	//create output directory is not exists
//	     	File folder = new File(targetDir);
//	     	if (folder.exists()) {
//	     		FileUtils.deleteDirectory(folder);
//	     		folder.mkdirs();
//	     	} else {
//	     		folder.mkdirs();
//	     	}
	    	 
	    	//get the zip file content
	    	ZipInputStream zis = new ZipInputStream(this.getClass().getResourceAsStream("/configsets.zip"));
	    	//get the zipped file list entry
	    	ZipEntry ze = zis.getNextEntry();
	
	    	while(ze!=null){
	
	    	   String fileName = ze.getName();
	           File newFile = new File(targetDir + File.separator + fileName);
	
	           if (ze.isDirectory()) {
	        	   newFile.mkdirs();
	           } else {
		           System.out.println("file unzip : "+ newFile.getAbsoluteFile());
		
		            //create all non exists folders
		            //else you will hit FileNotFoundException for compressed folder
		            //new File(newFile.getParent()).mkdirs();
		
		            FileOutputStream fos = new FileOutputStream(newFile);
		
		            int len;
		            while ((len = zis.read(buffer)) > 0) {
		       		fos.write(buffer, 0, len);
		            }
		
		            fos.close();
	           }
	           ze = zis.getNextEntry();
	    	}
	
	    	zis.closeEntry();
	    	zis.close();
	
	    	System.out.println("Done");
	     } catch (Exception e) {
	    	 e.printStackTrace();
	     }
	}

	@PreDestroy()
	public void stop() {
        try {
            solrClient().close();
        } catch (Exception e) {
        }
	}
}
