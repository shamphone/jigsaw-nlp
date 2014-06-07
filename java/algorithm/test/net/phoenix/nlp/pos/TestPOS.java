package net.phoenix.nlp.pos;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.phoenix.nlp.Term;
import net.phoenix.nlp.Tokenizer;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * 
 * @author lixf
 * 
 */
public class TestPOS {
	
	
	@Test
	public void testNumber() throws IOException {
		String[] sentences = { 
				"壹肆零伍年7月11日（明永乐三年）明成祖命郑和率领庞大的二百四十多海船、二万七千四百名船员组成的船队远航，访问了30多个在西太平洋和印度洋的国家和地区，加深了中国同东南亚、东非的相互了解。",
				"永乐三年六月十五（1405年7月11日）奉诏出使西洋。同年冬，从南京龙江港启航，经太仓出海，永乐五年九月初二（1407年10月2日）回国。第一次下西洋人数据载有27800人。",
				"监丞五员。" };
		this.runtest(sentences);
	}
	@Test
	public void testDate() throws IOException {
		String[] sentences = { "壹肆零伍年7月11日（明永乐三年）郑和开始下西洋。 ",				
				"永乐三年六月十五（1405年7月11日）奉诏出使西洋。",
				"同年冬，从南京龙江港启航，经太仓出海，永乐五年九月初二（1407年10月2日）回国。"};
		this.runtest(sentences);
	}

	@Test
	public void testChinesePerson() throws IOException {
		String[] sentences = {"台艺人陈俊生出轨逼死女友 绝情兽行遭曝光" };
		this.runtest(sentences);
	}
	@Test
	public void testChinesePerson2() throws IOException {
		String[] sentences = {"孙健用java11.023.236重写了张华平老师的分词."};
		this.runtest(sentences);
	}

	@Test
	public void testForeignPerson() throws IOException {
		String[] sentences = { "IBM公司总裁杰克韦尔奇将访问中国同时和联想总经理保尔卡金森会谈。" };

		this.runtest(sentences);
	}

	@Test
	public void testOrginization() throws IOException {
		String[] sentences = {  "北京中科辅龙计算机技术有限公司正在招聘人才。" };
		this.runtest(sentences);
	}

	@Test
	public void testTraditional2Simple() throws IOException {
		String[] sentences = {
				"關注十八大：台港澳密集解讀十八大報告",  "关注十八大：台港澳密集解读十八大报告",
				"參選國民黨主席？ 胡志強首度鬆口稱“會考慮”" ,
				"参选国民党主席？ 胡志强首度鬆口称“会考虑”",
				"駁謝長廷“國民黨像東廠” 藍營吁其勿惡意嘲諷",
				"驳谢长廷“国民党像东厂” 蓝营吁其勿恶意嘲讽",
				"台藝人陳俊生出軌逼死女友 絕情獸行遭曝光",
				"台艺人陈俊生出轨逼死女友 绝情兽行遭曝光",
				"林益世想回高雄探母 法官警告勿有逃亡念頭",
				"林益世想回高雄探母 法官警告勿有逃亡念头",
				"吳伯雄談建言被誤解讀:盡點言責 絕對善意",
				"吴伯雄谈建言被误解读:尽点言责 绝对善意"		
		};
		this.runtest(sentences);

	}
	
	@Test
	public void testText() throws IOException {
		String[] texts = {"news.txt","news2.txt"};
		File folder = new File("D:\\google\\jigsaw-nlp\\data\\pos");
		Tokenizer analysizer = new StandardTokenizer(folder);
		for(String text : texts){
			System.out.println("======"+ text+"=========");
		List<String> lines =IOUtils.readLines(this.getClass().getClassLoader().getResourceAsStream(text));
		for(String line : lines){
			System.out.println("");
			for (Term term : analysizer.tokenize(line)) {
				System.out.print(term.getName() +"/"+ term.getNature()+"|");
			}
			System.out.println();
		}
		}
	}
	

	private void runtest(String[] sentences) throws IOException {
		File folder = new File("D:\\google\\jigsaw-nlp\\data\\pos");
		Tokenizer analysizer = new StandardTokenizer(folder);
		for (String sentence : sentences) {
			System.out.println();
			System.out.print("analysis1:");
			for (Term term : analysizer.tokenize(sentence)) {
				System.out.print(term.getName() +"/"+ term.getNature()+"|");
			}
			System.out.println();
//			System.out.print("analysis2:");
//			for (Term term : analysis2.tokenize(sentence)) {
//				System.out.print(term.getName() + "|");
//			}
//			System.out.println();
		}
	}
}
