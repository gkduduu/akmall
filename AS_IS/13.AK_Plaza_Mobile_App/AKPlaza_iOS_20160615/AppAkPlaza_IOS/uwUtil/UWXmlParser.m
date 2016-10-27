//
//  UWXmlParser.m
//  KissXMLTest
//
//  Created by uniwis on 11. 7. 21..
//  Copyright 2011 korea. All rights reserved.
//

#import "UWXmlParser.h"


#import "DDXML.h"
#import "DDXMLElementAdditions.h"

#import "AlertMassageUtil.h"




@implementation UWXmlParser
//@synthesize itemConnection;
@synthesize xmlData,parsedItems;



- (void)dealloc
{
    [super dealloc];

    //[itemConnection release];

    // 20150703 MINSEOK 오래된 코드로 인한 64비트 기기에서 튕김 현상
    //[xmlData release];

    //[parsedItems release];

}

-(void)initRequest:(NSURLRequest *)req
{
    [[NSURLCache sharedURLCache] removeAllCachedResponses];
    
    DLog(@"3.req retain count : %d ", [req retainCount]);
    
    
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
    
    
    [itemConnection release];
    itemConnection = nil;
    
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
    DLog(@"2. req retain count : %d ", [urlRequset retainCount]);
    
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
    
    DLog(@"-------err---------");
    DLog(@"Connection failed! Error - %@ %@", [(NSError *)sender localizedDescription], [[(NSError *)sender userInfo] objectForKey:NSURLErrorFailingURLStringErrorKey]);
    
    
    //MSG_NORMAL(@"재접속 부탁드립니다", @"확인");
    MSG_NORMAL(@"서버 접속이 원할하지 않았습니다\n재시도 부탁드립니다", @"확인");
    
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
    // Append the downloaded chunk of data.
    
    

    [self.xmlData appendData:data] ; 
    
    
    //DLog(@"appendData:data =====%s\r\n=======", [data bytes]);
    
    
}


- (void)parser1
{
  
    DLog(@"parser1 appendData:xmlData =====%s\r\n=======", [self.xmlData bytes]);
    
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
    DDXMLDocument *doc = [[DDXMLDocument alloc] initWithXMLString:[[[NSString alloc] initWithData:self.xmlData encoding:NSUTF8StringEncoding] autorelease] options:0 error:nil];
    
    
    
    NSArray *root = [doc nodesForXPath:[arrItems objectAtIndex:0] error:nil];
    
    
    
    for(DDXMLElement *sub in root){
        
        NSArray *sub_items = [sub nodesForXPath:[arrItems objectAtIndex:1] error:nil];
        
        for (DDXMLElement *item in sub_items) {
            
            NSMutableDictionary *items = [[[NSMutableDictionary alloc] init] autorelease];
            
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
    
    [doc release];
    
    doc = nil;
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
    
    DDXMLDocument *doc = [[DDXMLDocument alloc] initWithXMLString:[[[NSString alloc] initWithData:self.xmlData encoding:NSUTF8StringEncoding] autorelease] options:0 error:nil];
    
    NSArray *root = [doc nodesForXPath:[arrItems objectAtIndex:0] error:nil];
    
    //str = [str stringByReplacingOccurrencesOfString:@"+" withString:@" "];

    
    for(DDXMLElement *sub in root){
        
        for (int i=0; i<[[arrItems objectAtIndex:1] count]; i++) {
        
            NSArray *sub_items = [sub nodesForXPath:[[arrItems objectAtIndex:1] objectAtIndex:i] error:nil];
            
            //DLog(@"sub_items : %d", [sub_items count]);
            
            NSMutableArray *dicSubItems = [[[NSMutableArray alloc] init] autorelease];
            
            for(DDXMLElement *subs in sub_items){
                
                NSArray *sub = [subs nodesForXPath:[arrItems objectAtIndex:2] error:nil];
            
                //DLog(@"sub : %d", [sub count]);
                
                for (DDXMLElement *item in sub) {
                    
                    NSMutableDictionary *items = [[[NSMutableDictionary alloc] init] autorelease];
                    
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
    
    [doc release];
    
    doc = nil;
}



- (void)parser3
{
    
    DLog(@"parser3 appendData:xmlData =====%s\r\n=======", [self.xmlData bytes]);
    
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
    
    DDXMLDocument *doc = [[DDXMLDocument alloc] initWithXMLString:[[[NSString alloc] initWithData:self.xmlData encoding:NSUTF8StringEncoding] autorelease] options:0 error:nil];
    
    //첫번째 element
    NSArray *root = [doc nodesForXPath:[arrItems objectAtIndex:0] error:nil];
    
    //str = [str stringByReplacingOccurrencesOfString:@"+" withString:@" "];
    
    
    for(DDXMLElement *secondSub in root){
        
        //두번째 element가 여러개 임.. 
        for (int i=0; i<[[arrItems objectAtIndex:1] count]; i++) {
            
            //두번째 element가 여러개 중에 한개 가져오기 
            DLog(@"parser3 : %@",[[arrItems objectAtIndex:1] objectAtIndex:i]);
            
            NSArray *secondSub_items = [secondSub nodesForXPath:[[arrItems objectAtIndex:1] objectAtIndex:i] error:nil];
            
            //DLog(@"sub_items : %d", [sub_items count]);
        
            NSMutableArray *dicSubItems = [[[NSMutableArray alloc] init] autorelease];
            
            for(DDXMLElement *thirdSubs in secondSub_items){
                
                //세번째 element
                NSArray *thirdSubs_items = [thirdSubs nodesForXPath:[arrItems objectAtIndex:2] error:nil];
                
                //DLog(@"sub : %d", [sub count]);
                
                for (DDXMLElement *item in thirdSubs_items) {
                    
                    NSMutableDictionary *items = [[[NSMutableDictionary alloc] init] autorelease];
                    
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
    
    [doc release];
    
    doc = nil;
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
    
    isComplete = YES;
    

    //[NSRunLoop cancelPreviousPerformRequestsWithTarget:self]; 

    
}


@end
