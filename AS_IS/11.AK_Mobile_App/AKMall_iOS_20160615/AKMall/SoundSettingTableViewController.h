//
//  SoundSettingTableViewController.h
//  AKMall
//
//  Created by 한병일 on 2014. 6. 23..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <AudioToolbox/AudioToolbox.h>
#import "Define.h"

@interface SoundSettingTableViewController : UITableViewController

@property (nonatomic, retain)  NSArray *list;
@property (nonatomic) CFURLRef baseURL;
@end
