//
//  XmlParser.m
//  test
//
//  Created by 한병일 on 2014. 6. 10..
//  Copyright (c) 2014년 한병일. All rights reserved.
//

#import "XmlParser.h"

#define MSG_NORMAL(TITLE, FRIST_BUTTON_TITLE) [[[[UIAlertView alloc] initWithTitle:TITLE message:nil delegate:nil cancelButtonTitle:FRIST_BUTTON_TITLE otherButtonTitles:nil, nil] autorelease] show]


@implementation XmlParser

@synthesize xmlData,parsedItems;

-(void)initRequest:(NSURLRequest *)req
{
    [[NSURLCache sharedURLCache] removeAllCachedResponses];
    
    self.xmlData = nil;
    self.parsedItems = nil;
    
    itemConnection = [[NSURLConnection alloc] initWithRequest:req delegate:self];
    
    if (itemConnection) {
        self.xmlData = [NSMutableData data];
        self.parsedItems = [[NSMutableArray array]init];
        
        isComplete = NO;
        while (!isComplete) {
            [[NSRunLoop currentRunLoop] runUntilDate:[NSDate dateWithTimeIntervalSinceNow:1]]; //1초씩.. connectionDidFinishLoading 할때까지..
        }
        
    }
}

//url로 파싱하는 경우
-(id)initParser:(NSURL *)url andWithItems:(NSArray *)items
{
    return [self initParser:url andWithItems:items andWithParserType:(ParserType *)Parser1];
}

-(id)initParser:(NSURL *)url andWithItems:(NSArray *)items  andWithParserType:(ParserType *)ParserType
{
    if (self) {
        
        
        NSURLRequest *theRequest = [NSURLRequest requestWithURL:url];
        
        arrItems = items;
        
        nParserType = ParserType;
        
        [self initRequest:theRequest];
        
        
    }
    
    return self;
}


//url과 request params 넘길때
-(id)initOtherParser:(NSMutableURLRequest *)urlRequset  andWithItems:(NSArray *)items;
{
    return [self initOtherParser:urlRequset andWithItems:items andWithParserType:(ParserType *)Parser1];
}


-(id)initOtherParser:(NSMutableURLRequest *)urlRequset  andWithItems:(NSArray *)items andWithParserType:(ParserType *)ParserType
{
    if (self) {
        
        arrItems = items;
        nParserType = ParserType;
        
        [self initRequest:urlRequset];
        
    }
    
    return self;
}


-(void)parseError:(id)sender
{
    
//    NSLog(@"-------err---------");
//    NSLog(@"Connection failed! Error - %@ %@", [(NSError *)sender localizedDescription], [[(NSError *)sender userInfo] objectForKey:NSURLErrorFailingURLStringErrorKey]);
    
    UIAlertView *aletView = [[UIAlertView alloc] initWithTitle:@"서버 접속이 원할하지 않았습니다\n재시도 부탁드립니다" message:nil delegate:nil cancelButtonTitle:@"확인" otherButtonTitles:nil, nil];
    [aletView show];
    
    //[self.parsedItems addObject:nil];
    
    isComplete = YES;
}

#pragma mark NSURLConnection Delegate methods


- (NSCachedURLResponse *)connection:(NSURLConnection *)connection willCacheResponse:(NSCachedURLResponse *)cachedResponse {
    return nil;
}


- (void)connection:(NSURLConnection *)connection didFailWithError:(NSError *)error {
    [NSRunLoop cancelPreviousPerformRequestsWithTarget:self];
    [self performSelectorOnMainThread:@selector(parseError:) withObject:error waitUntilDone:NO];
}


- (void)connection:(NSURLConnection *)connection didReceiveData:(NSData *)data {
    [self.xmlData appendData:data] ;
}


- (void)parser1
{
//    NSLog(@"parser1 appendData:xmlData =====%s\r\n=======", [self.xmlData bytes]);
    
    NSString *xmlString = [NSString stringWithFormat:@"%s",[self.xmlData bytes]];
    @try {
        NSArray *splitArr = [xmlString componentsSeparatedByString:@";"];
        for(NSString *tmpStr in splitArr){
            if([tmpStr rangeOfString:@"loginToken"].location != NSNotFound){
                NSUserDefaults *UD = [NSUserDefaults standardUserDefaults];
                if (UD)
                {
                    [UD setObject:tmpStr forKey:@"loginToken"];
                }
                [UD synchronize];
            }
        }
    }
    @catch (NSException *exception) {
    }
    
    /*
     xml 구조 예시 >
     <root>
     <sub>
     
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     
     <c_node4>aaa</c_node4>
     <c_node5>bbb</c_node5>
     <c_node6>ccc</c_node6>
     
     </sub>
     </root>
     
     */
    DDXMLDocument *doc = [[DDXMLDocument alloc] initWithXMLString:[[NSString alloc] initWithData:self.xmlData encoding:NSUTF8StringEncoding] options:0 error:nil];
    
    
    
    NSArray *root = [doc nodesForXPath:[arrItems objectAtIndex:0] error:nil];
    
    
    
    for(DDXMLElement *sub in root){
        
        NSArray *sub_items = [sub nodesForXPath:[arrItems objectAtIndex:1] error:nil];
        
        for (DDXMLElement *item in sub_items) {
            
            NSMutableDictionary *items = [[NSMutableDictionary alloc] init];
            
            for (int i=2; i<[arrItems count] ; i++) {
                DDXMLElement *element = [item elementForName:[arrItems objectAtIndex:i] ];
                if (element) {
                    NSString *elementValue = [NSString stringWithFormat:@"%@", element.stringValue];
                    
                    [items setObject:[[elementValue stringByReplacingOccurrencesOfString:@"+" withString:@" "] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding] forKey:[arrItems objectAtIndex:i]];
                    
                }
            }
            
            
            [self.parsedItems addObject:items];
            
            
        }
    }
}



//현재 사용안함.
- (void)parser2
{
    
    //외부 서버에서 한글인코딩 안해서 넘어오는 xml이어서 한글인코딩 해야함.
    /*
     xml 구조 예시 >
     <root>
     <sub1>
     <p_node>
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     </p_node>
     <p_node>
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     </p_node>
     <p_node>
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     </p_node>
     </sub1>
     <sub2>
     <p_node>
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     </p_node>
     <p_node>
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     </p_node>
     <p_node>
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     </p_node>
     </sub2>
     <sub3>
     <p_node>
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     </p_node>
     <p_node>
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     </p_node>
     <p_node>
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     </p_node>
     </sub3>
     </root>
     
     */
    //DDXMLDocument *doc = [[DDXMLDocument alloc] initWithData:xmlData options:0 error:nil];
    DDXMLDocument *doc = [[DDXMLDocument alloc] initWithXMLString:[[NSString alloc] initWithData:self.xmlData encoding:NSUTF8StringEncoding] options:0 error:nil];
    
    NSArray *root = [doc nodesForXPath:[arrItems objectAtIndex:0] error:nil];
    
    //str = [str stringByReplacingOccurrencesOfString:@"+" withString:@" "];
    
    
    for(DDXMLElement *sub in root){
        
        for (int i=0; i<[[arrItems objectAtIndex:1] count]; i++) {
            
            NSArray *sub_items = [sub nodesForXPath:[[arrItems objectAtIndex:1] objectAtIndex:i] error:nil];
            
            //NSLog(@"sub_items : %d", [sub_items count]);
            
            NSMutableArray *dicSubItems = [[NSMutableArray alloc] init];
            
            for(DDXMLElement *subs in sub_items){
                
                NSArray *sub = [subs nodesForXPath:[arrItems objectAtIndex:2] error:nil];
                
                //NSLog(@"sub : %d", [sub count]);
                
                for (DDXMLElement *item in sub) {
                    
                    NSMutableDictionary *items = [[NSMutableDictionary alloc] init];
                    
                    for (int i=3; i<[arrItems count] ; i++) {
                        DDXMLElement *element = [item elementForName:[arrItems objectAtIndex:i]];
                        if (element) {
                            NSString *elementValue = [NSString stringWithFormat:@"%@", element.stringValue];
                            [items setObject:[[elementValue stringByReplacingOccurrencesOfString:@"+" withString:@" "] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding] forKey:[arrItems objectAtIndex:i]];
                            
                        }
                    } //for (int i=3; i<[arrItems count] ; i++) {
                    
                    [dicSubItems addObject:items];
                    
                } //for (DDXMLElement *item in sub_items) {
                
            } //for(DDXMLElement *subs in sub_items){
            
            [self.parsedItems addObject:dicSubItems];
            
        } //for (int i=0; i<[[arrItems objectAtIndex:1] count]; i++) {
    }
}

- (void)parser3
{
    
//    NSLog(@"parser3 appendData:xmlData =====%@\r\n=======", [NSString stringWithUTF8String:[self.xmlData bytes]]);
    
    //외부 서버에서 한글인코딩 안해서 넘어오는 xml이어서 한글인코딩 해야함.
    
    
    
    
    /*
     xml 구조 예시 >
     <root>
     <sub1>
     <p_node>
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     </p_node>
     <p_node>
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     </p_node>
     <p_node>
     <c_node1>aaa</c_node1>
     <c_node2>bbb</c_node2>
     <c_node3>ccc</c_node3>
     </p_node>
     </sub1>
     <sub2>
     <p_node>
     <c_nodeA>aaa</c_nodeA>
     <c_nodeB>bbb</c_nodeB>
     <c_nodeC>ccc</c_nodeC>
     </p_node>
     <p_node>
     <c_nodeA>aaa</c_nodeA>
     <c_nodeB>bbb</c_nodeB>
     <c_nodeC>ccc</c_nodeC>
     </p_node>
     <p_node>
     <c_nodeA>aaa</c_nodeA>
     <c_nodeB>bbb</c_nodeB>
     <c_nodeC>ccc</c_nodeC>
     </p_node>
     </sub2>
     </root>
     
     */
    //DDXMLDocument *doc = [[DDXMLDocument alloc] initWithData:xmlData options:0 error:nil];
    
    DDXMLDocument *doc = [[DDXMLDocument alloc] initWithXMLString:[[NSString alloc] initWithData:self.xmlData encoding:NSUTF8StringEncoding] options:0 error:nil];
    
    //첫번째 element
    NSArray *root = [doc nodesForXPath:[arrItems objectAtIndex:0] error:nil];
    
    //str = [str stringByReplacingOccurrencesOfString:@"+" withString:@" "];
    
    
    for(DDXMLElement *secondSub in root){
        
        //두번째 element가 여러개 임..
        for (int i=0; i<[[arrItems objectAtIndex:1] count]; i++) {
            
            //두번째 element가 여러개 중에 한개 가져오기
//            NSLog(@"parser3 : %@",[[arrItems objectAtIndex:1] objectAtIndex:i]);
            
            NSArray *secondSub_items = [secondSub nodesForXPath:[[arrItems objectAtIndex:1] objectAtIndex:i] error:nil];
            
            //NSLog(@"sub_items : %d", [sub_items count]);
            
            NSMutableArray *dicSubItems = [[NSMutableArray alloc] init];
            
            for(DDXMLElement *thirdSubs in secondSub_items){
                
                //세번째 element
                NSArray *thirdSubs_items = [thirdSubs nodesForXPath:[arrItems objectAtIndex:2] error:nil];
                
                //NSLog(@"sub : %d", [sub count]);
                
                for (DDXMLElement *item in thirdSubs_items) {
                    
                    NSMutableDictionary *items = [[NSMutableDictionary alloc] init];
                    
                    //네번째 element
                    NSDictionary *elements = [arrItems objectAtIndex:3];
                    
                    //네번째 element - 두번째 element가 가지고 있는 각각의 element의 집합
                    NSArray *arrElements = [elements objectForKey:[[arrItems objectAtIndex:1] objectAtIndex:i]];
                    
                    for (int x=0; x<[arrElements count] ; x++) {
                        
                        DDXMLElement *element = [item elementForName:[arrElements objectAtIndex:x]];
                        if (element) {
                            NSString *elementValue = [NSString stringWithFormat:@"%@", element.stringValue];
                            [items setObject:[[elementValue stringByReplacingOccurrencesOfString:@"+" withString:@" "] stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding] forKey:[arrElements objectAtIndex:x]];
                            
                        }
                        
                    } //for (int i=3; i<[arrItems count] ; i++) {
                    
                    [dicSubItems addObject:items];
                    
                } //for (DDXMLElement *item in sub_items) {
                
            } //for(DDXMLElement *subs in sub_items){
            
            [self.parsedItems addObject:dicSubItems];
            
        } //for (int i=0; i<[[arrItems objectAtIndex:1] count]; i++) {
    }
}


- (void)connectionDidFinishLoading:(NSURLConnection *)connection {
    
    if (nParserType ==  (ParserType *)Parser1) {
        //[self parser1];
        [self performSelectorOnMainThread:@selector(parser1) withObject:nil waitUntilDone:NO];
    }
    else if (nParserType ==  (ParserType *)Parser2)
    {
        //[self parser2];
        [self performSelectorOnMainThread:@selector(parser2) withObject:nil waitUntilDone:NO];
    }
    else if (nParserType ==  (ParserType *)Parser3)
    {
        //[self parser3];
        [self performSelectorOnMainThread:@selector(parser3) withObject:nil waitUntilDone:NO];
    }
    else if (nParserType ==  (ParserType *)Parser4)
    {
        [self parser3];
    }
    else if (nParserType ==  (ParserType *)Parser5)
    {
        [self parser1];
    }
    
    isComplete = YES;
}

@end
