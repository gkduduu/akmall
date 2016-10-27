//
//  TimeSettingTableTableViewController.h
//  AKMall
//
//  Created by 한병일 on 2014. 6. 22..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Define.h"
#import "XmlParser.h"

@interface TimeSettingTableTableViewController : UITableViewController <UIActionSheetDelegate>

@property (nonatomic, retain) NSString *strStartDate;
@property (nonatomic, retain) NSString *strEndDate;
@property (nonatomic, retain) UIButton *notiSwitch;

@end
