//
//  SettingTableViewController.h
//  AKMall
//
//  Created by 한병일 on 2014. 6. 20..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "QuartzCore/QuartzCore.h"
#import "Define.h"
#import "XmlParser.h"
#import "TimeSettingTableTableViewController.h"

@interface SettingTableViewController : UITableViewController<UIAlertViewDelegate>
{
    BOOL isLoginInfo;
}
@property (nonatomic, retain) NSMutableArray *arrNotilist;
@property (nonatomic, retain) NSMutableArray *arrNotiItems;
@property (nonatomic, retain) NSMutableArray *arrNotiSpecialdayItems;
@property (nonatomic, retain) NSMutableArray *arrNotiDenytimeItems;

@property (nonatomic, retain) UIActivityIndicatorView *activityView;

@end
