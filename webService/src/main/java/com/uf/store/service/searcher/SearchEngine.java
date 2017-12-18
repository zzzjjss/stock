package com.uf.store.service.searcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.uf.store.dao.mysql.po.Product;
import com.uf.store.service.ProductManageService;
@Component
public class SearchEngine {
	private Logger logger=LoggerFactory.getLogger(SearchEngine.class);
	@Autowired
	private ProductManageService  productManageService;
	private Directory directory = null;
	private String indexDir = System.getProperty("user.dir");
	private boolean rebuildIndex = false;

	public SearchEngine	(@Value("${searcher.index.folder}")String indexDir,@Value("${searcher.reindex}") boolean rebuildIndex) {
		this.indexDir = indexDir;
		this.rebuildIndex = rebuildIndex;
	}
	@PostConstruct
	public void init() {
		try {
			Path path = Paths.get(indexDir);
			if (rebuildIndex) {
				FileUtils.deleteDirectory(new File(indexDir));
			}
			directory = FSDirectory.open(path);
			if(rebuildIndex){
				int index=0,pageSize=20;
				Page<Product> result=productManageService.getPagedProducts(0, pageSize, null);
				for(Product p:result.getContent()){
					this.addProductInfoToIndex(p);
				}
				int totalPage=result.getTotalPages();
				for(index=index+1;index<=totalPage;index++){
					result=productManageService.getPagedProducts(index,pageSize,null);
					for(Product p:result.getContent()){
						this.addProductInfoToIndex(p);
					}
				}
			}
		} catch (IOException e) {
			logger.error("",e);
		}
	}

	public void addProductInfoToIndex(Product product) {
		IndexWriter iWriter = null;
		try {
			Analyzer analyzer = new WhitespaceAnalyzer();
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			iWriter = new IndexWriter(directory, config);
			Document doc = new Document();
			StringField idField = new StringField("id", String.valueOf(product.getId()), Store.YES);
			TextField productField = new TextField("product", product.getSearchKeywords(), Store.YES);
			doc.add(idField);
			doc.add(productField);
			iWriter.addDocument(doc);
		} catch (Exception e) {
			logger.error("",e);
		} finally {
			if (iWriter != null) {
				try {
					iWriter.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
		}

	}

	public void listAllDocuments() {
		try {
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);
			MatchAllDocsQuery query = new MatchAllDocsQuery();
			TopDocs docs = isearcher.search(query, Integer.MAX_VALUE);
			for (ScoreDoc sdoc : docs.scoreDocs) {
				System.out.println("score:-->" + sdoc.score);
				Document searcheddoc = isearcher.doc(sdoc.doc);
				System.out.println(searcheddoc.getField("id").stringValue() + "---->>>"
						+ searcheddoc.getField("product").stringValue());
			}
		} catch (Exception e) {
			logger.error("",e);
		}

	}

	public List<Long> searchProductIds(String keyword) {
		List<Long> ids = new ArrayList<Long>();
		try {
			DirectoryReader ireader = DirectoryReader.open(directory);
			IndexSearcher isearcher = new IndexSearcher(ireader);
			// Analyzer analyzer = new WhitespaceAnalyzer();
			// QueryParser parser=new QueryParser("product",analyzer);
			BooleanQuery.Builder builder = new BooleanQuery.Builder();

			if (!Strings.isNullOrEmpty(keyword)) {
				List<String> keywords = Splitter.on(" ").omitEmptyStrings().trimResults().splitToList(keyword);
				for (String key : keywords) {
					Query query = new TermQuery(new Term("product", key));
					builder.add(query, Occur.SHOULD);
				}
				TopDocs topDocs = isearcher.search(builder.build(), Integer.MAX_VALUE);
				// if(topDocs.scoreDocs==null||topDocs.scoreDocs.length<=0){
				// builder = new BooleanQuery.Builder();
				// for (String key : keywords) {
				// Query query = new TermQuery(new Term("product", key));
				// builder.add(query, Occur.SHOULD);
				// }
				// topDocs=isearcher.search(builder.build(),Integer.MAX_VALUE);
				// }
				for (ScoreDoc sdoc : topDocs.scoreDocs) {
					System.out.println("score:-->" + sdoc.score);
					Document searcheddoc = isearcher.doc(sdoc.doc);
					ids.add(Long.parseLong(searcheddoc.getField("id").stringValue()));
					System.out.println(searcheddoc.getField("id").stringValue() + "---->>>" + searcheddoc.getField("product").stringValue());
				}
			}

		} catch (Exception e) {
			logger.error("",e);
		}
		return ids;
	}

	public void deleteProductFromIndexById(Integer productId) {
		IndexWriter iWriter = null;
		try {
			Analyzer analyzer = new WhitespaceAnalyzer();
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			iWriter = new IndexWriter(directory, config);
			iWriter.deleteDocuments(new Term("id", String.valueOf(productId)));
			;
		} catch (Exception e) {
			logger.error("",e);
		} finally {
			if (iWriter != null) {
				try {
					iWriter.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
		}
	}

	public boolean updateProductIndex(Product product) {
		IndexWriter iWriter = null;
		try {
			Analyzer analyzer = new WhitespaceAnalyzer();
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			iWriter = new IndexWriter(directory, config);
			TextField productField = new TextField("product", product.getSearchKeywords(), Store.YES);
			StringField idField = new StringField("id", String.valueOf(product.getId()), Store.YES);
			List<IndexableField> fields = new ArrayList<IndexableField>();
			fields.add(productField);
			fields.add(idField);
			iWriter.updateDocument(new Term("id", String.valueOf(product.getId())), fields);
		} catch (Exception e) {
			logger.error("",e);
			return false;
		} finally {
			if (iWriter != null) {
				try {
					iWriter.close();
				} catch (IOException e) {
					logger.error("",e);
				}
			}
		}
		return true;
	}

	public void listAllTerm() {
		try {
			IndexReader indexReader = DirectoryReader.open(directory);
			Fields fields = MultiFields.getFields(indexReader);
			Iterator<String> fieldsIterator = fields.iterator();
			while (fieldsIterator.hasNext()) {
				String field = fieldsIterator.next();
				Terms terms = fields.terms(field);
				TermsEnum termsEnums = terms.iterator();
				BytesRef byteRef = null;
				logger.info("field : " + field);
				while ((byteRef = termsEnums.next()) != null) {
					String term = new String(byteRef.bytes, byteRef.offset, byteRef.length);
					logger.info("term is : " + term);
				}
			}

		} catch (IOException e) {
			logger.error("",e);
		}
	}
}
