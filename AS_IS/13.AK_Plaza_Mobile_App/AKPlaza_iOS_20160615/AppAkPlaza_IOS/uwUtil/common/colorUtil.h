//전체 배경 
#define kBgColor RGB(255, 255, 255)

//로그인 정보 하단 bg칼라 
#define kLoginInfoBgColor RGB(204, 197, 183)

//타이틀 배경 
#define kTitleBgColorBrown RGB(255,255,255)

//타이틀 보더
#define kTitleBorderColor RGB(50,47,42)
#define kTitleBorderWidth 2.0f

#define kTableViewSeparatorColor RGB(204,204,204)




//폰트 

//폰트컬러 : White
#define kFontColorWhite RGB(255,255,255)

//타이틀 폰트컬러 
#define kFontTitleColor RGB(22,22,22)




//기본 폰트컬러 : normal
#define kFontBrownColor RGB(115, 103, 91)
#define kFontDefaultFontColor RGB(100, 100, 100)

//폰트컬러 : sligth
#define kFontGrayColor RGB(171, 159, 144)

//폰트컬러 : weighty  테이블뷰 셀 text color
#define kFontDarkBrownColor RGB(52, 47, 42)

//폰트컬러 : weighty  테이블뷰 셀 text color
#define kFontWeightiernColor RGB(178, 72, 72)

//폰트컬러 : 악세사리뷰
#define kFontAccSaColor RGB(138, 138, 138)




/*
//화이트컬러 : RGB 255,255,255
#define kFontColorWhite [UIColor colorWithWhite:0.757 alpha:1.000]

//짙은 브라운, 기본칼라, RGB 103,94,86
#define kFontBrownColor [UIColor colorWithRed:0.404 green:0.369 blue:0.337 alpha:1.000]

//포인트컬러 (베이지) : RGB  169,164.156
#define kFontGrayColor [UIColor colorWithRed:0.663 green:0.643 blue:0.612 alpha:1.000]


//타이틀 연한 베이지 RGB  211,216,204
#define kFontTitleColor [UIColor colorWithRed:0.867 green:0.824 blue:0.800 alpha:1.000]

//타이틀바 RGB 90,80,70
#define kTitleBgColorBrown [UIColor colorWithRed:0.353 green:0.314 blue:0.275 alpha:1.000]

//기본배경 : RGB 229, 226, 213
#define kBgColor [UIColor colorWithRed:0.898 green:0.886 blue:0.835 alpha:1.000]


//로그인 정보 하단 bg칼라 
#define kLoginInfoBgColor [UIColor colorWithRed:0.800 green:0.773 blue:0.718 alpha:1.000]

*/

#define RGB(r, g, b) [UIColor colorWithRed:r/255.0 green:g/255.0 blue:b/255.0 alpha:1]
#define RGBA(r, g, b, a) [UIColor colorWithRed:r/255.0 green:g/255.0 blue:b/255.0 alpha:a]

#define UIColorFromRGB(rgbValue) [UIColor colorWithRed:((float)((rgbValue & 0xFF0000) >> 16))/255.0 green:((float)((rgbValue & 0xFF00) >> 8))/255.0 blue:((float)(rgbValue & 0xFF))/255.0 alpha:1.0]

//알림보관함 체크 컬러
#define AlarmColor [UIColor colorWithRed:204/255.0 green:204/255.0 blue:204/255.0 alpha:1]