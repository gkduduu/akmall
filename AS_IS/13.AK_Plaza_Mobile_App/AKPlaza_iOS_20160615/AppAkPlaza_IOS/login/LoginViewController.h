//
//  LoginViewController.h
//  testAKMall06
//
//  Created by uniwis on 11. 5. 6..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UWNavigation.h"
#import "colorUtil.h"
#import "fontSizeUtil.h"
#import "AkLoginModel.h"
#import "OLogin.h"

@interface LoginViewController : UWNavigation <baseModelDelegate, UINavigationControllerDelegate, UIAlertViewDelegate, UINavigationBarDelegate, UITextFieldDelegate>{
    
    UIButton *btnLogin;
    UIButton *btnTextFieldHidden;
    UITextField *txtUserID;
    UITextField *txtUserPassword;
    
    //로그인 처리 ~ 
    NSString *queryString;
    NSData *queryData;
    NSURLConnection *urlCon;
    NSMutableData *rawData;
    NSMutableDictionary *dicData;
    
    
    BOOL isAutoLogin;
    
    
    
}



@property (nonatomic, retain) IBOutlet UITextField *txtUserID;
@property (nonatomic, retain) IBOutlet UITextField *txtUserPassword;

@property (nonatomic, retain) IBOutlet UIButton *btnLogin;
@property (nonatomic, retain) UIActivityIndicatorView *activityView;




@property (nonatomic) BOOL isLeftBtn;



- (IBAction)onBtnBackground:(id)sender;
- (void)onCheckBox:(id)sender;
- (IBAction)procLogin:(id)sender;



@property (nonatomic, retain) NSString *queryString;
@property (nonatomic, retain) NSData *queryData;
@property (nonatomic, retain) NSURLConnection *urlCon;
@property (nonatomic, retain) NSMutableData *rawData;
@property (nonatomic, retain) NSMutableDictionary *dicData;





@end
