package com.TODAY.HtmlParserByJuyoungJin ;

import android.app.Activity ;
import android.os.Bundle ;
import android.os.StrictMode;
import android.widget.* ;
import java.io.BufferedReader ;
import java.io.InputStreamReader ;
import java.util.StringTokenizer;

import org.apache.http.* ;
import org.apache.http.client.methods.HttpGet ;
import org.apache.http.impl.client.DefaultHttpClient ;

import com.TODAY.R;

import java.util.* ;

public class HTMLParsingActivity extends Activity
{
	EditText text ;

	private String url = "http://203.237.226.95:8080/mobile/login/login_ok.jsp?userid=32071467&userpw=jj119&returnUrl=../m7/m7_c1.jsp&instanceid=" ;
	public TimeTable[] DownloadHtml()
	{				
		//---------------------------------------------------------------------------
		//	Android ���ø����̼ǿ��� ���ͳݿ� �����ϱ� ���ؼ��� ���ͳ� ���� ������ �ʿ��ϴ�
		//---------------------------------------------------------------------------
		//	-	AndroidManifest.xml ���Ͽ� ���� ������ �ο��ϴ� ����� �߰��Ѵ� (�׸� ����)
		//---------------------------------------------------------------------------

		//---------------------------------------------------------------------------
		//	Apache Commons Library�� �̿��� HTML �о����
		//---------------------------------------------------------------------------
		//	-	Java������ 'URL Class'�� 'URLConnection Class'�� �̿��ؼ� HTTP ����� �� �� �ִ�
		//	-	Android������ Commons�� 'HttpClient Interface'�� ������ Class��
		//		DefaultHttpClient Class�� ������ HTTP ����� ����� �� �ִ�
		//	-	Android���� 'Apache HttpClient Library'�� �⺻���� ���ԵǾ� �ִ�
		//---------------------------------------------------------------------------
		//	HttpGet(url)	
		//	-	�ȵ���̵忡�� HTML�� �о���� ������� ���
		//	-	�α����� ���� Get ��� ��� (�ּҿ� ID�� ��й�ȣ�� ����Ͽ� ���)
		//---------------------------------------------------------------------------

		DefaultHttpClient client = new DefaultHttpClient() ;
		StringBuilder html = new StringBuilder() ;

		//---------------------------------------------------------------------------
		//	Internet ������ �ȵ� ��츦 ����Ͽ� Try/Catch�� �̿��Ѵ�
		//---------------------------------------------------------------------------

		try
		{
			HttpGet httpget = new HttpGet(url) ;
			HttpResponse response = client.execute(httpget) ;

			// �Է½�Ʈ���� ������ ����

			InputStreamReader isr = new InputStreamReader(response.getEntity().getContent()) ;

			// ������ ���پ� �б� ���ؼ� BufferedReader�� ����

			BufferedReader br = new BufferedReader(isr) ;

			// ������ ù���� ������ �����ؼ� null ���� ���ѵ� ������ ��ٸ� ������ �����Ѵ�

			String line = br.readLine() ;

			while (line != null)
			{
				// ���پ� �о���̸鼭 ���κп� New Line ����
				html.append(line + '\n') ;
				line = br.readLine() ;
			}
			br.close() ;
		}
		catch (Exception e)
		{
			e.printStackTrace() ;
		}

		//---------------------------------------------------------------------------
		//	�ʿ��� �ð�ǥ �κ��� ���۰� �� �ε����� ���ѵ� �߶󳽴�
		//---------------------------------------------------------------------------

		int SP = html.indexOf("<tbody id=\"table_contents_1\">") + 31 ;
		int EP = html.indexOf("</tbody>") ;
		String mResult = new String(html.toString().substring(SP, EP)) ;

		//---------------------------------------------------------------------------
		//	���ʿ��� HTML code �κ��� �����Ѵ�
		//---------------------------------------------------------------------------

		mResult = mResult.replaceAll("<tr>", "") ;
		mResult = mResult.replaceAll("</tr>", "") ;
		mResult = mResult.replaceAll("<td>", "") ;
		mResult = mResult.replaceAll("</td>", "") ;
		mResult = mResult.replaceAll("<th>", "") ;
		mResult = mResult.replaceAll("</th>", "") ;
		mResult = mResult.replaceAll("<th colspan=\"2\">", "") ;
		mResult = mResult.replaceAll("<th colspan=\"3\">", "") ;
		mResult = mResult.replaceAll("<td colspan=\"2\">", "") ;
		mResult = mResult.replaceAll("<td colspan=\"3\">", "") ;
		mResult = mResult.replaceAll("\t", "") ;
		mResult = mResult.replaceAll("\n\n\n", "\n") ;
		mResult = mResult.replaceAll("\n\n", "\n") ;

		//---------------------------------------------------------------------------

		StringTokenizer ST = new StringTokenizer(mResult, "\n") ;
		ST.nextToken() ;
		ST.nextToken() ;

		TimeTable[] TT = new TimeTable[(ST.countTokens()-2)/6] ;
		int i = 0 ;

		while((ST.countTokens()-2) > 0)
		{
			TT[i] = new TimeTable(
					Integer.parseInt(ST.nextToken()),
					Integer.parseInt(ST.nextToken()),
					Integer.parseInt(ST.nextToken()),
					ST.nextToken(),
					ST.nextToken(),
					ST.nextToken()) ;
			i++ ;
		}
		return TT ;
	}
	public String DayInt2String(int iDay)
	{
		String mDay = new String() ;
		switch(iDay)
		{
		case 1:		mDay = "��" ; break ;
		case 2:		mDay = "��" ; break ;
		case 3:		mDay = "ȭ" ; break ;
		case 4:		mDay = "��" ; break ;
		case 5:		mDay = "��" ; break ;
		case 6:		mDay = "��" ; break ;
		case 7:		mDay = "��" ; break ;
		default :	mDay = "��" ;
		}		
		return mDay ;
	}
	public String setTodayTT(TimeTable TT[])
	{
		//  �Ͽ���:1 ~ �����:7

		Calendar cal= Calendar.getInstance( ) ;
		String mDay = new String(DayInt2String(cal.get(Calendar.DAY_OF_WEEK))) ;
				
		StringBuilder mTodayTT = new StringBuilder() ;
				
		TimeZone jst = TimeZone.getTimeZone ("JST") ;
		Calendar mCal = Calendar.getInstance ( jst ) ;
		mTodayTT.append(mCal.get(Calendar.YEAR) + "�� " + (mCal.get(Calendar.MONTH)+1) + "�� " + mCal.get(Calendar.DATE) + "�� " + mDay + "����\n");
		
		for(int i=0; i<TT.length; i++)
		{
			if(TT[i].getTime().indexOf(mDay) > -1)
			{	
				mTodayTT.append(TT[i].getTitle()) ;
				mTodayTT.append(" ") ;
				mTodayTT.append(TT[i].getTime()) ;
				mTodayTT.append(" ") ;
				mTodayTT.append(TT[i].getProfessor()) ;
				mTodayTT.append("\n") ;
			}
		}
		return mTodayTT.toString() ; 
	}
	public void onCreate(Bundle savedInstanceState)
	{
		text = (EditText)findViewById(R.id.mTV) ;
		
		

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// HTTP Access�� ���� SDK 3.0 ���� �������� �ݵ�� ����� �ϴ� ����
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);        

		text = (EditText)findViewById(R.id.mTV) ;
		text.setText(setTodayTT(DownloadHtml())) ;
	}
}
