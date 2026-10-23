package ir.mehrineh.family

import android.app.*
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.*
import android.content.Intent
import android.net.Uri

class MainActivity:Activity() {
 private lateinit var root:LinearLayout
 private val navy=0xFF192447.toInt();private val pink=0xFFDB4478.toInt()
 private val prefs by lazy { getSharedPreferences("family_v5",MODE_PRIVATE) }
 private fun dp(n:Int)=(n*resources.displayMetrics.density).toInt()
 private fun shape(c:Int,r:Int=20)=GradientDrawable().apply {setColor(c);cornerRadius=dp(r).toFloat()}
 private fun label(s:String,size:Float=17f,bold:Boolean=false)=TextView(this).apply {text=s;textSize=size;setTextColor(navy);gravity=Gravity.RIGHT or Gravity.CENTER_VERTICAL;textDirection=View.TEXT_DIRECTION_RTL;if(bold)setTypeface(null,Typeface.BOLD);setPadding(dp(12),dp(9),dp(12),dp(9))}
 private fun card(title:String,detail:String,emoji:String="♡",action:(()->Unit)?=null) {
  val outer=LinearLayout(this).apply {orientation=LinearLayout.HORIZONTAL;gravity=Gravity.CENTER_VERTICAL;layoutDirection=View.LAYOUT_DIRECTION_RTL;background=shape(Color.WHITE);elevation=dp(3).toFloat();setPadding(dp(10),dp(12),dp(10),dp(12))}
  val icon=label(emoji,29f).apply {gravity=Gravity.CENTER;background=shape(0xFFF9E8F0.toInt(),16)};outer.addView(icon,LinearLayout.LayoutParams(dp(67),dp(67)))
  val col=LinearLayout(this).apply {orientation=LinearLayout.VERTICAL};col.addView(label(title,19f,true));col.addView(label(detail,13f));outer.addView(col,LinearLayout.LayoutParams(0,-2,1f))
  outer.addView(label("‹",28f));root.addView(outer,LinearLayout.LayoutParams(-1,-2).apply {bottomMargin=dp(13)});if(action!=null)outer.setOnClickListener{action()}
 }
 private fun title(s:String){root.addView(label(s,22f,true),LinearLayout.LayoutParams(-1,-2).apply {topMargin=dp(15);bottomMargin=dp(8)})}
 private fun info(s:String){AlertDialog.Builder(this).setTitle("مهرینه خانواده").setMessage(s).setPositiveButton("متوجه شدم",null).show()}
 override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);window.statusBarColor=0xFFFBF9FD.toInt();window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;setContentView(R.layout.activity_main);root=findViewById(R.id.root);root.alpha=0f;root.animate().alpha(1f).setDuration(650).start();render()}
 private fun render(){root.removeAllViews();root.setPadding(dp(15),dp(12),dp(15),dp(24));root.setBackgroundColor(0xFFFBF9FD.toInt())
  root.addView(label("پنل خانواده مهرینه",27f,true))
  card("همراه خانواده","مراقبت با احترام به حریم خصوصی سالمند","🏡") {connect()}
  title("وضعیت عزیزتان")
  card("آخرین اعلام حال","هنوز داده‌ای از گوشی سالمند دریافت نشده","♡") {connect()}
  card("درخواست کمک","هیچ اعلان زنده‌ای متصل نیست","🛡") {connect()}
  card("یادآوری‌ها","پس از اتصال، برنامه‌های دارویی نمایش داده می‌شوند","▦") {connect()}
  card("پیشرفت آموزش","پیشرفت درس‌ها پس از همگام‌سازی قابل مشاهده است","📖") {connect()}
  card("موقعیت مکانی سالمند","اشتراک موقعیت غیرفعال است؛ نیازمند رضایت سالمند","📍") {info("مکان واقعی سالمند فقط پس از دریافت اجازه صریح در گوشی او و راه‌اندازی اتصال امن نمایش داده می‌شود. این نسخه موقعیت را حدس نمی‌زند و ردیابی پنهانی انجام نمی‌دهد.")}
  title("دسترسی سریع خانواده")
  card("شماره‌های تماس","ذخیره شماره و تماس با سالمند","☎") {phone()}
  card("برنامه‌های روزانه","برنامه‌های روزانه پس از اتصال نمایش داده می‌شوند","📅") {connect()}
  card("دستیار خانواده","پنج سؤال آماده برای راهنمایی خانواده","💬") {assistant()}
  card("اتصال و حریم خصوصی","وضعیت: هنوز متصل نشده","🔐") {connect()}
 }
 private fun connect(){info("برای نمایش واقعی وضعیت سلامت، مکان و اعلان‌های سالمند، باید اتصال امن بین دو اپ و اجازه سالمند فعال شود. فعلاً داده زنده دریافت نمی‌شود.")}
 private fun phone(){val input=EditText(this).apply {inputType=android.text.InputType.TYPE_CLASS_PHONE;setText(prefs.getString("phone",""))};AlertDialog.Builder(this).setTitle("شماره سالمند").setView(input).setPositiveButton("ذخیره"){_,_->prefs.edit().putString("phone",input.text.toString()).apply()}.setNeutralButton("تماس"){_,_->val n=input.text.toString();if(n.isNotBlank())startActivity(Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+Uri.encode(n))))}.setNegativeButton("انصراف",null).show()}
 private fun assistant(){root.removeAllViews();root.addView(label("دستیار خانواده 💬",25f,true));root.addView(label("سؤال‌های متداول مراقبت از سالمند",15f));val chat=LinearLayout(this).apply {orientation=LinearLayout.VERTICAL};root.addView(chat)
  val questions=listOf(
   "چطور از حال سالمند باخبر شوم؟" to "پس از اتصال امن دو برنامه و رضایت سالمند، آخرین اعلام حال او در داشبورد خانواده نمایش داده می‌شود.",
   "اگر سالمند درخواست کمک کرد چه کنم؟" to "درخواست کمک باید از مسیر اعلان امن ارسال شود. برای وضعیت اضطراری، مستقیماً تماس بگیرید و از خدمات اضطراری محلی کمک بخواهید.",
   "چطور داروهای سالمند را پیگیری کنم؟" to "در بخش یادآوری‌ها، پس از اتصال، وضعیت ثبت‌شده را می‌بینید. مصرف دارو را بدون تأیید سالمند قطعی فرض نکنید.",
   "چطور موقعیت سالمند را ببینم؟" to "موقعیت فقط با اجازه روشن و قابل لغو سالمند، هنگام فعال بودن اشتراک‌گذاری، قابل مشاهده خواهد بود.",
   "چطور به سالمند در یادگیری کمک کنم؟" to "از بخش پیشرفت آموزش، درس‌های انجام‌شده را پس از اتصال ببینید و او را به تماشای آموزش‌های کوتاه تشویق کنید."
  )
  questions.forEach { (q,a)->val b=Button(this).apply {text=q;textSize=15f;isAllCaps=false;background=shape(0xFFF7E9F0.toInt());setTextColor(navy);setOnClickListener {val u=label("شما: $q",16f,true).apply {background=shape(0xFFFFE0EB.toInt())};val r=label("دستیار: $a",16f).apply {background=shape(0xFFF0EEFF.toInt());alpha=0f};chat.addView(u);chat.addView(r);r.animate().alpha(1f).setDuration(450).start()} };root.addView(b,LinearLayout.LayoutParams(-1,dp(64)).apply {bottomMargin=dp(7)}) }
  val back=Button(this).apply {text="بازگشت به داشبورد";setOnClickListener{render()}};root.addView(back)
 }
}
