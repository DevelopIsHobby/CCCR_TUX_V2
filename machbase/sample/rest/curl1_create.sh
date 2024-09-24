curl -G "http://127.0.0.1:5657/machbase" --data-urlencode 'q=drop table curl_sample'
echo ""
curl -G "http://127.0.0.1:5657/machbase" --data-urlencode 'q=create table curl_sample (c1 int, c2 varchar(20))'
echo ""
curl -G "http://127.0.0.1:5657/machbase/columns/curl_sample"
echo ""
