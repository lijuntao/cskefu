/*
 * Copyright (C) 2023 Beijing Huaxia Chunsong Technology Co., Ltd. 
 * <https://www.chatopera.com>, Licensed under the Chunsong Public 
 * License, Version 1.0  (the "License"), https://docs.cskefu.com/licenses/v1.html
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Copyright (C) 2018- Jun. 2023 Chatopera Inc, <https://www.chatopera.com>,  Licensed under the Apache License, Version 2.0, 
 * http://www.apache.org/licenses/LICENSE-2.0
 * Copyright (C) 2017 优客服-多渠道客服系统,  Licensed under the Apache License, Version 2.0, 
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package com.cskefu.cc.controller.resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cskefu.cc.controller.Handler;
import com.cskefu.cc.model.Organ;
import com.cskefu.cc.model.User;
import com.cskefu.cc.persistence.repository.OrganRepository;
import com.cskefu.cc.persistence.repository.UserRepository;
import com.cskefu.cc.util.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/res")
public class UsersResourceController extends Handler {
    @Autowired
    private UserRepository userRes;

    @Autowired
    private OrganRepository organRes;

    @RequestMapping("/users")
    @Menu(type = "res", subtype = "users")
    @ResponseBody
    public String add(ModelMap map, HttpServletRequest request, @Valid String q, @Valid String id) {
        if (q == null) {
            q = "";
        }
        Page<User> usersList = getUsers(request, q);
        JSONArray result = new JSONArray();
        for (User user : usersList.getContent()) {
            JSONObject item = new JSONObject();
            item.put("id", user.getId());
            item.put("text", user.getUsername() + "(" + user.getUname() + ")");
            result.add(item);
        }

        return result.toJSONString();
    }

    @RequestMapping("/bpm/users")
    @Menu(type = "res", subtype = "users")
    public ModelAndView bpmusers(ModelMap map, HttpServletRequest request, @Valid String q, @Valid String id) {
        if (q == null) {
            q = "";
        }
        map.addAttribute("usersList", getUsers(request, q));
        return request(super.createView("/public/bpmusers"));
    }

    @RequestMapping("/bpm/organ")
    @Menu(type = "res", subtype = "users")
    public ModelAndView organ(ModelMap map, HttpServletRequest request, @Valid String q, @Valid String ids) {
        map.addAttribute("organList", getOrgans(request));
        map.addAttribute("usersList", getUsers(request));
        map.addAttribute("ids", ids);
        return request(super.createView("/public/organ"));
    }

    private List<User> getUsers(HttpServletRequest request) {
        return userRes.findByDatastatus(false);
    }

    /**
     * 获取当前产品下人员信息
     *
     * @param request
     * @param q
     * @return
     */
    private Page<User> getUsers(HttpServletRequest request, String q) {
        if (q == null) {
            q = "";
        }
        Page<User> list = userRes.findByDatastatusAndUsernameLike(false, "%" + q + "%", PageRequest.of(0, 10));
        return list;
    }

    /**
     * 获取当前产品下 技能组 组织信息
     *
     * @param request
     * @return
     */
    private List<Organ> getOrgans(HttpServletRequest request) {
        List<Organ> list = organRes.findBySkill(true);
        return list;
    }
}
